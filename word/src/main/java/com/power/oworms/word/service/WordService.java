package com.power.oworms.word.service;

import com.power.oworms.auth.service.SettingsService;
import com.power.oworms.common.error.OWormException;
import com.power.oworms.common.error.OWormExceptionType;
import com.power.oworms.mail.service.EmailService;
import com.power.oworms.word.domain.PartOfSpeech;
import com.power.oworms.word.domain.Word;
import com.power.oworms.word.dto.StatisticsDTO;
import com.power.oworms.word.dto.WordDTO;
import com.power.oworms.word.dto.WordRequestDTO;
import com.power.oworms.word.mapper.WordMapper;
import com.power.oworms.word.repository.WordRepository;
import com.power.oworms.word.util.FilterUtil;
import com.power.oworms.word.util.StatsUtil;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class WordService {

    private final WordRepository repository;
    private final EmailService emailService;
    private final TagService tagService;
    private final SettingsService ss;
    private final Bucket bucket;

    @Value("${oxford.api.url}")
    private String oxfordApiURL;

    @Value("${oxford.app.id}")
    private String oxfordAppId;

    @Value("${oxford.app.key}")
    private String oxfordAppKey;

    public WordService(final WordRepository repository,
                       final EmailService emailService,
                       final TagService tagService,
                       final SettingsService ss) {
        this.repository = repository;
        this.emailService = emailService;
        this.tagService = tagService;
        this.ss = ss;
        this.bucket = Bucket.builder().addLimit(Bandwidth.classic(300, Refill.greedy(300, Duration.ofDays(1)))).build();
    }

    @Transactional
    public WordDTO create(final WordRequestDTO wordRequestDTO, String uname, String banana) {
        consumeToken();
        ss.permit(uname, banana);

        if (wordExists(wordRequestDTO.getWord())) {
            throw new OWormException(OWormExceptionType.WORD_EXISTS, "That word already exists");
        }

        final Word word = WordMapper.map(wordRequestDTO.getWord());

        word.setCreatedBy(uname);
        word.setCreationDate(LocalDateTime.now());
        repository.saveAndFlush(word);

        tagService.updateTagsForWord(word.getId(), wordRequestDTO.getTagIds());

        int numberOfWords = (int) repository.count();
        WordDTO createdWord = WordMapper.map(word);

        emailService.sendNewWordEmail("oworms | word #" + numberOfWords + " added", WordMapper.mapToEmailDTO(createdWord));

        return createdWord;
    }

    private boolean wordExists(WordDTO wordDTO) {
        return repository.findByTheWordIgnoreCase(wordDTO.getTheWord()).isPresent();
    }

    public List<WordDTO> retrieveAll(String word,
                                     List<String> pos,
                                     String def,
                                     String origin,
                                     String example,
                                     List<String> tags,
                                     String note,
                                     String creator) {
        consumeToken();

        final List<Word> words = repository.findAll();

        final List<Word> filteredWords = FilterUtil.filter(
                words,
                word,
                pos,
                def,
                origin,
                example,
                tags,
                note,
                creator
        );

        if (filteredWords.isEmpty()) {
            throw new OWormException(OWormExceptionType.NOT_FOUND, "No words were found");
        }

        return WordMapper.mapTo(filteredWords);
    }

    public WordDTO retrieve(final Long wordId) {
        consumeToken();

        final Word word = findById(wordId);

        word.setTimesViewed(word.getTimesViewed() + 1);
        repository.save(word);

        return WordMapper.map(word);
    }

    public WordDTO retrieveRandom() {
        consumeToken();

        final List<Word> words = repository.findAll();

        Random rand;
        int randomIndex; // 0 (inclusive) -> arg (exclusive)
        try {
            rand = SecureRandom.getInstanceStrong();
            randomIndex = rand.nextInt((int) repository.count());
        } catch (NoSuchAlgorithmException e) {
            throw new OWormException(OWormExceptionType.FAILURE, "Error while retrieving random word");
        }

        Word randomWord = words.get(randomIndex);

        return WordMapper.map(randomWord);
    }

    public ResponseEntity<String> oxfordRetrieve(String theWord, String uname, String banana) {
        consumeToken();
        ss.permit(uname, banana);

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("app_id", oxfordAppId);
        headers.set("app_key", oxfordAppKey);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        String url = oxfordApiURL.replace("{theWord}", theWord);

        try {
            return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (RestClientException e) {
            throw new OWormException(OWormExceptionType.FAILURE, "Error while searching Oxford API", e.getMessage());
        }
    }

    public WordDTO update(Long wordId, WordRequestDTO wordRequestDTO, String uname, String banana) {
        consumeToken();
        ss.permit(uname, banana);

        Word word = findById(wordId);
        WordDTO oldWordDTO = WordMapper.map(word);

        WordDTO uWordDTO = wordRequestDTO.getWord();

        boolean alreadyExists = repository.findByTheWordIgnoreCaseAndIdNot(uWordDTO.getTheWord(), wordId).isPresent();
        if (alreadyExists) {
            throw new OWormException(OWormExceptionType.WORD_EXISTS, "That word already exists");
        }

        word.setTheWord(uWordDTO.getTheWord());
        word.setDefinition(uWordDTO.getDefinition());
        word.setPartOfSpeech(PartOfSpeech.getPartOfSpeech(uWordDTO.getPartOfSpeech()));
        word.setPronunciation(uWordDTO.getPronunciation());
        word.setOrigin(uWordDTO.getOrigin());
        word.setExampleUsage(uWordDTO.getExampleUsage());
        word.setNote(uWordDTO.getNote());
        // creationDate, createdBy, and timesViewed cannot be modified.

        word = repository.saveAndFlush(word);

        tagService.updateTagsForWord(word.getId(), wordRequestDTO.getTagIds());

        emailService.sendUpdateWordEmail(
                "oworms | word #" + word.getId() + " updated",
                WordMapper.mapToEmailDTO(oldWordDTO),
                WordMapper.mapToEmailDTO(WordMapper.map(word))
        );

        return uWordDTO;
    }

    private Word findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new OWormException(OWormExceptionType.NOT_FOUND, "A word with an ID of " + id + " does not exist"));
    }

    public StatisticsDTO getStatistics() {
        consumeToken();

        StatisticsDTO stats = new StatisticsDTO();

        List<Word> words = repository.findAll();

        int totalWords = (int) repository.count();
        stats.setTotalWords(totalWords);

        int totalViewsOnWords = words
                .parallelStream()
                .reduce(0, (acc, current) -> acc + current.getTimesViewed(), Integer::sum);
        stats.setTotalViewsOnWords(totalViewsOnWords);

        StatsUtil.getPartsOfSpeechStats(stats, words);
        StatsUtil.getFirstLetterStats(stats, words);

        return stats;
    }

    private void consumeToken() {
        if (!bucket.tryConsume(1)) {
            throw new OWormException(OWormExceptionType.REQUEST_LIMIT_EXCEEDED, "You have made too many requests");
        }
    }
}

package com.power.service;

import com.power.domain.PartOfSpeech;
import com.power.domain.Word;
import com.power.dto.WordDTO;
import com.power.dto.WordRequestDTO;
import com.power.error.OWormException;
import com.power.error.OWormExceptionType;
import com.power.mapper.WordMapper;
import com.power.repository.WordRepository;
import com.power.util.FilterUtil;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Service
public class WordService {

    private final WordRepository repository;
    private final EmailService emailService;
    private final HelperService helperService;
    private final TagService tagService;

    @Value("${oxford.api.url}")
    private String oxfordApiURL;

    @Value("${oxford.app.id}")
    private String oxfordAppId;

    @Value("${oxford.app.key}")
    private String oxfordAppKey;

    public WordService(final WordRepository repository,
                       final EmailService emailService,
                       final HelperService helperService,
                       final TagService tagService) {
        this.repository = repository;
        this.emailService = emailService;
        this.helperService = helperService;
        this.tagService = tagService;
    }

    @Transactional
    public WordDTO create(final WordRequestDTO wordRequestDTO, String permissionKey, String user) {
        helperService.checkPermission(permissionKey);
        helperService.checkActionLimit();

        if (wordExists(wordRequestDTO.getWord())) {
            throw new OWormException(OWormExceptionType.WORD_EXISTS, "That word already exists");
        }

        final Word word = WordMapper.map(wordRequestDTO.getWord());

        word.setCreatedBy(user);
        word.setCreationDate(LocalDateTime.now());
        repository.saveAndFlush(word);

        tagService.updateTagsForWord(word.getId(), wordRequestDTO.getTagIds(), permissionKey);

        int numberOfWords = (int) repository.count();
        WordDTO createdWord = WordMapper.map(word);

        emailService.sendNewWordEmail("oworms | word #" + numberOfWords + " added", createdWord);

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
        helperService.checkRetrievalLimit();

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
        helperService.checkRetrievalLimit();

        final Word word = findById(wordId);

        word.setTimesViewed(word.getTimesViewed() + 1);
        repository.save(word);

        WordDTO wordDTO = WordMapper.map(word);

        return wordDTO;
    }

    public WordDTO retrieveRandom() {
        helperService.checkRetrievalLimit();

        final List<Word> words = repository.findAll();

        Random rand;
        int randomIndex; // 0 (inclusive) -> arg (exclusive)
        try {
            rand = SecureRandom.getInstanceStrong();
            randomIndex = rand.nextInt((int) repository.count());
        } catch (NoSuchAlgorithmException e) {
            throw new OWormException(OWormExceptionType.GENERAL_FAILURE, "Error while retrieving random word");
        }

        Word randomWord = words.get(randomIndex);

        return WordMapper.map(randomWord);
    }

    public ResponseEntity<String> oxfordRetrieve(String theWord, String permissionKey) {
        helperService.checkPermission(permissionKey);
        helperService.checkRetrievalLimit();

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("app_id", oxfordAppId);
        headers.set("app_key", oxfordAppKey);

        HttpEntity<String> entity = new HttpEntity<>("body", headers);

        String url = oxfordApiURL.replace("{theWord}", theWord);

        try {
            return restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        } catch (RestClientException e) {
            throw new OWormException(OWormExceptionType.GENERAL_FAILURE, "Error while searching Oxford API", e.getMessage());
        }
    }

    public WordDTO update(Long wordId, WordRequestDTO wordRequestDTO, String permissionKey) {
        helperService.checkPermission(permissionKey);
        helperService.checkActionLimit();

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

        tagService.updateTagsForWord(word.getId(), wordRequestDTO.getTagIds(), permissionKey);

        emailService.sendUpdateWordEmail(
                "oworms | word #" + word.getId() + " updated",
                oldWordDTO,
                WordMapper.map(word)
        );

        return uWordDTO;
    }

    private Word findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new OWormException(OWormExceptionType.NOT_FOUND, "A word with an ID of " + id + " does not exist"));
    }
}

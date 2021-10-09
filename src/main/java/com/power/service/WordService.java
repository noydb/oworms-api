package com.power.service;

import com.power.domain.PartOfSpeech;
import com.power.domain.Word;
import com.power.dto.WordDTO;
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
    private final WordMapper mapper;
    private final EmailService emailService;
    private final HelperService helperService;

    @Value("${oxford.api.url}")
    private String oxfordApiURL;

    @Value("${oxford.app.id}")
    private String oxfordAppId;

    @Value("${oxford.app.key}")
    private String oxfordAppKey;

    public WordService(final WordRepository repository,
                       final WordMapper mapper,
                       final EmailService emailService,
                       final HelperService helperService) {
        this.repository = repository;
        this.mapper = mapper;
        this.emailService = emailService;
        this.helperService = helperService;
    }

    @Transactional
    public WordDTO create(final WordDTO wordDTO, String permissionKey, String user) {
        helperService.checkPermission(permissionKey);
        helperService.checkActionLimit();

        if (wordExists(wordDTO)) {
            throw new OWormException(OWormExceptionType.WORD_EXISTS, "The word, " + wordDTO.getTheWord() + " already exists");
        }

        final Word word = mapper.map(wordDTO);
        word.setCreatedBy(user);
        word.setCreationDate(LocalDateTime.now());

        repository.save(word);

        int numberOfWords = repository.findAll().size();
        WordDTO createdWord = mapper.map(word);

        emailService.sendNewWordEmail("oworms | word #" + numberOfWords + " added", createdWord);

        return createdWord;
    }

    private boolean wordExists(WordDTO wordDTO) {
        return repository.findByTheWordIgnoreCase(wordDTO.getTheWord()).isPresent();
    }

    public List<WordDTO> retrieveAll(String theWord,
                                     String definition,
                                     List<String> partsOfSpeech,
                                     String creator,
                                     String haveLearnt) {
        helperService.checkRetrievalLimit();

        final List<Word> words = repository.findAll();

        final List<Word> filteredWords = FilterUtil.filter(words, theWord, definition, partsOfSpeech, creator, haveLearnt);

        if (filteredWords.isEmpty()) {
            throw new OWormException(OWormExceptionType.NOT_FOUND, "No words were found");
        }

        return mapper.map(filteredWords);
    }

    public WordDTO retrieve(final Long wordId) {
        helperService.checkRetrievalLimit();

        final Word word = findById(wordId);

        word.setTimesViewed(word.getTimesViewed() + 1);
        repository.save(word);

        return mapper.map(word);
    }

    public WordDTO retrieveRandom() {
        helperService.checkRetrievalLimit();

        final List<Word> words = repository.findAll();

        Random rand;
        int randomIndex; // 0 (inclusive) -> arg (exclusive)
        try {
            rand = SecureRandom.getInstanceStrong();
            randomIndex = rand.nextInt(words.size());
        } catch (NoSuchAlgorithmException e) {
            throw new OWormException(OWormExceptionType.GENERAL_FAILURE, "Error while retrieving random word");
        }

        Word randomWord = words.get(randomIndex);

        return mapper.map(randomWord);
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

    public WordDTO update(Long wordId, WordDTO updatedWord, String permissionKey) {
        helperService.checkPermission(permissionKey);
        helperService.checkActionLimit();

        Word word = findById(wordId);
        WordDTO oldWordDTO = mapper.map(word);

        boolean alreadyExists = repository.findByTheWordIgnoreCaseAndIdNot(updatedWord.getTheWord(), wordId).isPresent();
        if (alreadyExists) {
            throw new OWormException(OWormExceptionType.WORD_EXISTS, "That word already exists");
        }

        word.setTheWord(updatedWord.getTheWord());
        word.setDefinition(updatedWord.getDefinition());
        word.setPartOfSpeech(PartOfSpeech.getPartOfSpeech(updatedWord.getPartOfSpeech()));
        word.setPronunciation(updatedWord.getPronunciation());
        word.setOrigin(updatedWord.getOrigin());
        word.setExampleUsage(updatedWord.getExampleUsage());
        word.setHaveLearnt(updatedWord.isHaveLearnt());
        // creationDate, createdBy, and timesViewed cannot be modified.

        word = repository.save(word);
        updatedWord = mapper.map(word);

        emailService.sendUpdateWordEmail(
                "oworms | word #" + word.getId() + " updated",
                oldWordDTO,
                updatedWord
        );

        return updatedWord;
    }

    private Word findById(Long id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new OWormException(OWormExceptionType.NOT_FOUND, "A word with an ID of " + id + " does not exist"));
    }
}

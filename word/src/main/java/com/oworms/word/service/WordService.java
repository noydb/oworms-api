package com.oworms.word.service;

import com.oworms.auth.dto.UserDTO;
import com.oworms.auth.service.SettingsService;
import com.oworms.auth.service.UserService;
import com.oworms.common.error.OWormException;
import com.oworms.common.error.OWormExceptionType;
import com.oworms.mail.dto.BucketOverflowDTO;
import com.oworms.mail.service.EmailService;
import com.oworms.word.domain.PartOfSpeech;
import com.oworms.word.domain.Word;
import com.oworms.word.dto.UserProfileDTO;
import com.oworms.word.dto.WordDTO;
import com.oworms.word.dto.WordFilter;
import com.oworms.word.dto.WordRequestDTO;
import com.oworms.word.mapper.WordMapper;
import com.oworms.word.repository.WordRepository;
import com.oworms.word.util.FilterUtil;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class WordService {

    private final WordRepository repository;
    private final EmailService emailService;
    private final TagService tagService;
    private final SettingsService ss;
    private final UserService userService;

    private final Bucket bucket;
    private static final String JHB_ZONE = "Africa/Johannesburg";

    public WordService(final WordRepository repository,
                       final EmailService emailService,
                       final TagService tagService,
                       final SettingsService ss,
                       final UserService userService) {
        this.repository = repository;
        this.emailService = emailService;
        this.tagService = tagService;
        this.ss = ss;
        this.userService = userService;
        this.bucket = Bucket
                .builder()
                .addLimit(Bandwidth.classic(350, Refill.greedy(350, Duration.ofDays(1)))).build();
    }

    @Transactional
    public WordDTO create(final WordRequestDTO wordRequestDTO, String u, String banana) {
        consumeToken("create");
        final UserDTO loggedInUser = ss.permit(u, banana);

        final Optional<Word> existingOpt = repository.findByTheWordIgnoreCase(wordRequestDTO.getWord().getTheWord());
        if (existingOpt.isPresent()) {
            throw new OWormException(OWormExceptionType.ALREADY_EXISTS, "that word already exists uuid:" + existingOpt.get().getUuid());
        }

        final Word word = WordMapper.map(wordRequestDTO.getWord());

        word.setCreatedBy(loggedInUser.getUsername());
        word.setCreationDate(OffsetDateTime.now(ZoneId.of(JHB_ZONE)));
        repository.saveAndFlush(word);

        tagService.updateTagsForWord(word.getId(), wordRequestDTO.getTagIds());

        int numberOfWords = (int) repository.count();
        WordDTO createdWord = WordMapper.map(word);

        emailService.sendNewWordEmail(
                "oworms | word #" + numberOfWords + " added",
                WordMapper.mapToEmailDTO(createdWord),
                userService.getRecipientsForEmail()
        );

        return createdWord;
    }

    public List<WordDTO> retrieveAll(final WordFilter wordFilter) {
        consumeToken("all words");

        final List<Word> words = repository.findAll();
        final int totalWordCount = words.size();
        final int noOfWordsToReturn = wordFilter.getNumberOfWords();

        final List<Word> filteredWords = FilterUtil
                .filter(words, wordFilter)
                .subList(0, Math.min(noOfWordsToReturn, totalWordCount));

        if (filteredWords.isEmpty()) {
            throw new OWormException(OWormExceptionType.NOT_FOUND, "No words were found");
        }

        return WordMapper.mapTo(filteredWords);
    }

    @Transactional
    public WordDTO retrieve(final String uuid) {
        consumeToken("word retrieve");

        final Word word = findByUuid(uuid);

        word.setTimesViewed(word.getTimesViewed() + 1);
        repository.save(word);

        return WordMapper.map(word);
    }

    public WordDTO retrieveRandom() {
        consumeToken("random");

        final List<Word> words = repository.findAll();

        if (words.isEmpty()) {
            final WordDTO defaultDTO = new WordDTO();
            defaultDTO.setTheWord("Create a word!");
            defaultDTO.setCreatedBy("oworms");

            return defaultDTO;
        }

        Random rand;
        int randomIndex; // 0 (inclusive) -> arg (exclusive)
        try {
            rand = SecureRandom.getInstanceStrong();
            randomIndex = rand.nextInt(words.size()); // must be positive
        } catch (NoSuchAlgorithmException e) {
            throw new OWormException(OWormExceptionType.FAILURE, "Error while retrieving random word");
        }

        return WordMapper.map(words.get(randomIndex));
    }

    public WordDTO update(final String uuid, final WordRequestDTO wordRequestDTO, final String u, final String banana) {
        consumeToken(u);
        ss.permit(u, banana);

        Word word = findByUuid(uuid);
        final WordDTO oldWord = WordMapper.map(word);

        WordDTO uWordDTO = wordRequestDTO.getWord();

        boolean alreadyExists = repository.findByTheWordIgnoreCaseAndUuidNot(uWordDTO.getTheWord(), uuid).isPresent();
        if (alreadyExists) {
            throw new OWormException(OWormExceptionType.ALREADY_EXISTS, "That word already exists");
        }

        word.setTheWord(uWordDTO.getTheWord());
        word.setDefinition(uWordDTO.getDefinition());
        word.setPartOfSpeech(PartOfSpeech.getPartOfSpeech(uWordDTO.getPartOfSpeech()));
        word.setPronunciation(uWordDTO.getPronunciation());
        word.setOrigin(uWordDTO.getOrigin());
        word.setExampleUsage(uWordDTO.getExampleUsage());
        word.setNote(uWordDTO.getNote());
        // creationDate, createdBy, and timesViewed cannot be modified

        final Word updatedWord = repository.saveAndFlush(word);

        tagService.updateTagsForWord(word.getId(), wordRequestDTO.getTagIds());

        emailService.sendUpdateWordEmail(
                WordMapper.mapToUpdateEmailDTO(oldWord, WordMapper.map(updatedWord)),
                userService.getRecipientsForEmail()
        );

        return uWordDTO;
    }

    public UserProfileDTO getUserData(final String u, final String banana) {
        consumeToken(u);

        final UserDTO user = userService.retrieve(u, banana);
        final List<String> likedWords = user.getLikedWordUUIDs();

        final long wordsCreated = repository.countByCreatedByEquals(u);
        final List<WordDTO> likedWordDTOs = new ArrayList<>();

        repository.findAll().forEach(word -> {
            if (likedWords.contains(word.getUuid())) {
                likedWordDTOs.add(WordMapper.map(word));
            }
        });

        return new UserProfileDTO((int) wordsCreated, likedWordDTOs);
    }

    private Word findByUuid(final String uuid) {
        return repository
                .findByUuid(uuid)
                .orElseThrow(() -> new OWormException(OWormExceptionType.NOT_FOUND, "Word with uuid: " + uuid + " does not exist"));
    }

    private void consumeToken(final String context) {
        if (!bucket.tryConsume(1)) {
            emailService.sendBucketOverflow(new BucketOverflowDTO(this.getClass().getName(), context));

            throw new OWormException(OWormExceptionType.REQUEST_LIMIT_EXCEEDED, "You have made too many requests");
        }
    }
}

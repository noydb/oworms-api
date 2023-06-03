package com.oworms.word.service;

import com.oworms.auth.dto.UserDTO;
import com.oworms.auth.service.SettingsService;
import com.oworms.auth.service.UserService;
import com.oworms.error.OWormException;
import com.oworms.error.OWormExceptionType;
import com.oworms.util.Utils;
import com.oworms.mail.dto.BucketOverflowDTO;
import com.oworms.mail.service.EmailService;
import com.oworms.word.domain.PartOfSpeech;
import com.oworms.word.domain.Word;
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
    public WordDTO create(final WordRequestDTO wordRequestDTO, final String u, final String banana) {
        consumeToken("create");
        final UserDTO loggedInUser = permit(u, banana, "create");

        final String theNewWord = wordRequestDTO.getWord().getTheWord().trim();

        final Optional<Word> existingOpt = repository.findByTheWordIgnoreCase(theNewWord);
        if (existingOpt.isPresent()) {
            throw new OWormException(
                    OWormExceptionType.CONFLICT,
                    "that word already exists with ID: " + existingOpt.get().getUuid()
            );
        }

        final Word word = WordMapper.map(wordRequestDTO.getWord());
        word.setCreationDate(Utils.now());
        word.setCreatedBy(loggedInUser.getUsername());
        word.setTheWord(theNewWord);

        repository.saveAndFlush(word);
        tagService.updateTagsForWord(word.getId(), wordRequestDTO.getTagIds());

        final long numberOfWords = repository.count();
        WordDTO createdWord = WordMapper.map(word);

        emailService.sendNewWordEmail(
                "oworms | word #" + numberOfWords + " added",
                WordMapper.mapToEmailDTO(createdWord),
                userService.getRecipientsForEmail()
        );

        return createdWord;
    }

    public List<WordDTO> retrieveAll(final WordFilter wordFilter) {
        consumeToken("retrieve all");

        final List<Word> words = repository.findAll();
        final int noOfWordsToReturn = wordFilter.getNumberOfWords();

        final List<Word> filteredWords = FilterUtil.filter(words, wordFilter);
        final int totalWordCount = filteredWords.size();

        if (filteredWords.isEmpty()) {
            throw new OWormException(OWormExceptionType.NOT_FOUND, "No words were found");
        }

        return WordMapper.mapTo(filteredWords.subList(0, Math.min(noOfWordsToReturn, totalWordCount)));
    }

    @Transactional
    public WordDTO retrieve(final String uuid) {
        consumeToken("retrieve");

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
        permit(u, banana, "update");

        final Word word = findByUuid(uuid);
        final WordDTO oldWord = WordMapper.map(word);
        WordDTO uWordDTO = wordRequestDTO.getWord();

        final Optional<Word> existingOpt = repository.findByTheWordIgnoreCaseAndUuidNot(uWordDTO.getTheWord(), uuid);
        if (existingOpt.isPresent()) {
            throw new OWormException(
                    OWormExceptionType.CONFLICT,
                    "that word already exists with ID: " + existingOpt.get().getUuid()
            );
        }

        word.setTheWord(uWordDTO.getTheWord());
        word.setDefinition(uWordDTO.getDefinition());
        word.setPartOfSpeech(PartOfSpeech.getPartOfSpeech(uWordDTO.getPartOfSpeech()));
        word.setPronunciation(uWordDTO.getPronunciation());
        word.setOrigin(uWordDTO.getOrigin());
        word.setExampleUsage(uWordDTO.getExampleUsage());
        word.setDiscoveredAt(uWordDTO.getDiscoveredAt());
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

    private Word findByUuid(final String uuid) {
        return repository
                .findByUuid(uuid)
                .orElseThrow(() -> new OWormException(OWormExceptionType.NOT_FOUND, "Word with uuid: " + uuid + " does not exist"));
    }

    private UserDTO permit(final String u, final String banana, final String context) {
        consumeToken(context);

        try {
            return ss.permit(u, banana);
        } catch (final OWormException e) {
            throw new OWormException(OWormExceptionType.INSUFFICIENT_RIGHTS, "You cannot do that");
        }
    }

    private void consumeToken(final String context) {
        if (!bucket.tryConsume(1)) {
            emailService.sendBucketOverflow(new BucketOverflowDTO(this.getClass().getName(), context));

            throw new OWormException(OWormExceptionType.REQUEST_LIMIT_EXCEEDED, "You have made too many requests");
        }
    }
}

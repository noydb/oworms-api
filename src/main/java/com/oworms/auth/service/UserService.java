package com.oworms.auth.service;

import com.oworms.auth.domain.User;
import com.oworms.auth.dto.UserDTO;
import com.oworms.auth.error.AccountExistsException;
import com.oworms.auth.mapper.UserMapper;
import com.oworms.auth.repository.UserRepository;
import com.oworms.common.error.OWormException;
import com.oworms.common.error.OWormExceptionType;
import com.oworms.mail.dto.BucketOverflowDTO;
import com.oworms.mail.service.EmailService;
import com.oworms.word.domain.Word;
import com.oworms.word.dto.WordDTO;
import com.oworms.word.mapper.WordMapper;
import com.oworms.word.repository.WordRepository;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.Duration;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;
    private final SettingsService ss;
    private final EmailService emailService;
    private final Bucket bucket;

    private final WordRepository wordRepository;

    @Autowired
    public UserService(final UserRepository repository,
                       final SettingsService ss,
                       final EmailService emailService,
                       final WordRepository wordRepository) {
        this.repository = repository;
        this.ss = ss;
        this.emailService = emailService;
        this.wordRepository = wordRepository;
        this.bucket = Bucket
                .builder()
                .addLimit(Bandwidth.classic(300, Refill.greedy(300, Duration.ofDays(300)))).build();
    }

    public UserDTO retrieve(final String username, final String bna) {
        consumeToken("retrieve");
        ss.permit(username, bna);

        final User user = repository
                .findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("no such user"));
        final List<String> likedWordUUIDs = user.getLikedWordUUIDs();

        final UserDTO userDTO = UserMapper.mapUser(user);
        userDTO.setCreatedWordCount((int) wordRepository.countByCreatedByEquals(user.getUsername()));

        final List<WordDTO> likedWords = new ArrayList<>();
        wordRepository.findAll().forEach(word -> {
            if (likedWordUUIDs.contains(word.getUuid())) {
                likedWords.add(WordMapper.map(word));
            }
        });

        userDTO.setLikedWords(
                likedWords
                        .stream()
                        .sorted(Comparator.comparing(WordDTO::getTheWord))
                        .collect(Collectors.toList())
        );

        return userDTO;
    }

    @Transactional
    public void updateUser(String userUUID, UserDTO userDTO, String username, String banana) throws AccountExistsException {
        consumeToken("update");
        ss.permit(username, banana);

        final User existingUser = repository
                .findByUuid(userUUID)
                .orElseThrow(() -> new OWormException(OWormExceptionType.NOT_FOUND, "A user with that id does not exist"));
        repository
                .findByUuidNotAndUsername(userUUID, userDTO.getUsername())
                .ifPresent(user -> {
                    throw new OWormException(OWormExceptionType.CONFLICT, "That username is already in use");
                });
        repository
                .findByUuidNotAndEmail(userUUID, userDTO.getUsername())
                .ifPresent(user -> {
                    throw new OWormException(OWormExceptionType.CONFLICT, "That email is already in use");
                });

        final String newUsername = userDTO.getUsername().trim();
        if (!newUsername.equals(existingUser.getUsername())) {
            final List<Word> words = wordRepository.findAllByCreatedBy(existingUser.getUsername());
            for (final Word word : words) {
                word.setCreatedBy(newUsername);
            }
            wordRepository.saveAllAndFlush(words);
        }

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());

        repository.save(existingUser);
    }

    @Transactional
    public void likeWord(String wordUUID, String username, String banana) {
        consumeToken("like word");
        ss.permit(username, banana);

        User user = repository
                .findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("no such user"));

        final List<String> existing = user.getLikedWordUUIDs();

        if (existing == null || existing.isEmpty()) {
            user.setLikedWordUUIDs(new ArrayList<>(Collections.singletonList(wordUUID)));
        } else {
            final List<String> filtered = existing
                    .stream()
                    .filter(existingId -> !existingId.equals(wordUUID))
                    .collect(Collectors.toList());

            if (filtered.size() == existing.size()) {
                existing.add(wordUUID);
            } else {
                // the word has been unliked
                user.setLikedWordUUIDs(filtered);
            }
        }

        repository.save(user);
    }

    public String[] getRecipientsForEmail() {
        return repository
                .findAll()
                .stream()
                .map(User::getEmail)
                .toArray(String[]::new);
    }

    private void consumeToken(String context) {
        if (!bucket.tryConsume(1)) {
            emailService.sendBucketOverflow(new BucketOverflowDTO(this.getClass().getName(), context));

            throw new OWormException(OWormExceptionType.REQUEST_LIMIT_EXCEEDED, "You have made too many requests");
        }
    }
}

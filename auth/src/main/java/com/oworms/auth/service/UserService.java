package com.oworms.auth.service;

import com.oworms.auth.domain.Status;
import com.oworms.auth.domain.User;
import com.oworms.auth.dto.NewUserDTO;
import com.oworms.auth.dto.UserDTO;
import com.oworms.auth.error.AccountExistsException;
import com.oworms.auth.mapper.UserMapper;
import com.oworms.auth.repository.UserRepository;
import com.oworms.common.error.OWormException;
import com.oworms.common.error.OWormExceptionType;
import com.oworms.mail.dto.BucketOverflowDTO;
import com.oworms.mail.service.EmailService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository repository;
    private final SettingsService ss;
    private final EmailService emailService;
    private final Bucket bucket;

    @Autowired
    public UserService(final UserRepository repository,
                       final SettingsService ss,
                       final EmailService emailService) {
        this.repository = repository;
        this.ss = ss;
        this.emailService = emailService;
        this.bucket = Bucket.builder().addLimit(Bandwidth.classic(5, Refill.greedy(5, Duration.ofDays(1)))).build();
    }

    @Transactional
    public void create(NewUserDTO newUserDTO, String username, String banana) throws AccountExistsException {
        consumeToken("create");
        ss.permit(username, banana);

        Optional<User> userOptional = repository.findByUsername(newUserDTO.getUsername());
        if (userOptional.isPresent()) {
            throw new AccountExistsException("An account already exists with the supplied username or email address");
        }

        User user = UserMapper.map(newUserDTO);

        repository.save(user);
    }

    public UserDTO retrieve(final String username, final String bna) {
        consumeToken("retrieve");
        ss.permit(username, bna);

        final User user = repository
                .findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("no such user"));

        final UserDTO userDTO = UserMapper.mapUser(user);

        return userDTO;
    }

    @Transactional
    public void updateUser(String userUUID, UserDTO userDTO, String username, String banana) throws AccountExistsException {
        consumeToken("update");
        ss.permit(username, banana);

        Optional<User> usernameOptional = repository.findByUuidNotAndUsername(userUUID, userDTO.getUsername());
        Optional<User> emailOptional = repository.findByUuidNotAndEmail(userUUID, userDTO.getEmail());

        if (usernameOptional.isPresent() || emailOptional.isPresent()) {
            throw new AccountExistsException("An account already exists with the supplied username or email address.");
        }

        User existingUser = repository
                .findByUuid(userUUID)
                .orElseThrow(() ->
                new EntityNotFoundException("A user with that ID does not exist"));

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmail(userDTO.getEmail());
        existingUser.setStatus(Status.getStatus(userDTO.getStatus()));

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

    private void consumeToken(String context) {
        if (!bucket.tryConsume(1)) {
            emailService.sendBucketOverflow(new BucketOverflowDTO(this.getClass().getName(), context));

            throw new OWormException(OWormExceptionType.REQUEST_LIMIT_EXCEEDED, "You have made too many requests");
        }
    }
}

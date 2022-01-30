package com.power.oworms.auth.service;

import com.power.oworms.auth.domain.Status;
import com.power.oworms.auth.domain.User;
import com.power.oworms.auth.dto.NewUserDTO;
import com.power.oworms.auth.dto.UpdatePasswordDTO;
import com.power.oworms.auth.dto.UserDTO;
import com.power.oworms.auth.error.AccountExistsException;
import com.power.oworms.auth.mapper.UserMapper;
import com.power.oworms.auth.repository.UserRepository;
import com.power.oworms.common.error.OWormException;
import com.power.oworms.common.error.OWormExceptionType;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.Duration;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
public class UserService {

    private final UserRepository repository;
    private final SettingsService ss;
    private final Bucket bucket;

    @Autowired
    public UserService(final UserRepository repository,
                       final SettingsService ss) {
        this.repository = repository;
        this.ss = ss;
        this.bucket = Bucket.builder().addLimit(Bandwidth.classic(300, Refill.greedy(5, Duration.ofDays(1)))).build();
    }

    @Transactional
    public void create(NewUserDTO newUserDTO, String username, String banana) throws AccountExistsException {
        consumeToken();
        ss.permit(username, banana);

        Optional<User> userOptional = repository.findByUsernameOrEmailAddress(newUserDTO.getUsername(), newUserDTO.getEmail());
        if (userOptional.isPresent()) {
            throw new AccountExistsException("An account already exists with the supplied username or email address");
        }

        User user = UserMapper.map(newUserDTO);
        user.setPassword(newUserDTO.getPassword());

        repository.save(user);
    }

    public UserDTO retrieve(String username) {
        User user = repository
                .findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new EntityNotFoundException("no such user"));

        return UserMapper.mapUser(user);
    }

    @Transactional
    public void updateUser(Long userId, UserDTO userDTO, String username, String banana) throws AccountExistsException {
        consumeToken();
        ss.permit(username, banana);

        Optional<User> usernameOptional = repository.findByIdNotAndUsername(userId, userDTO.getUsername());
        Optional<User> emailOptional = repository.findByIdNotAndEmailAddress(userId, userDTO.getEmail());

        if (usernameOptional.isPresent() || emailOptional.isPresent()) {
            throw new AccountExistsException("An account already exists with the supplied username or email address.");
        }

        User existingUser = repository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("A user with that ID does not exist"));

        existingUser.setUsername(userDTO.getUsername());
        existingUser.setEmailAddress(userDTO.getEmail());
        existingUser.setStatus(Status.getStatus(userDTO.getStatus()));

        // password cannot be updated in this service

        repository.save(existingUser);
    }

    @Transactional
    public void updatePassword(Long userId, UpdatePasswordDTO updatePasswordDTO, String username, String banana) {
        consumeToken();
        ss.permit(username, banana);

        User existingUser = repository.findById(userId).orElseThrow(() ->
                new EntityNotFoundException("A user with that ID does not exist"));

        existingUser.setPassword(updatePasswordDTO.getPassword());

        repository.save(existingUser);
    }

    private void consumeToken() {
        if (!bucket.tryConsume(1)) {
            throw new OWormException(OWormExceptionType.REQUEST_LIMIT_EXCEEDED, "You have made too many requests");
        }
    }
}

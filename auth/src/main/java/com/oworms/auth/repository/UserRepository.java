package com.oworms.auth.repository;

import com.oworms.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String usernameOrEmail);

    Optional<User> findByUuid(String uuid);

    Optional<User> findByUuidNotAndUsername(String uuid, String username);

    Optional<User> findByUuidNotAndEmail(String uuid, String email);

}

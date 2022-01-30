package com.power.oworms.auth.repository;

import com.power.oworms.auth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsernameIgnoreCase(String username);

    Optional<User> findByUsernameOrEmailAddress(String username, String emailAddress);

    Optional<User> findByIdNotAndUsername(Long id, String username);

    Optional<User> findByIdNotAndEmailAddress(Long id, String emailAddress);

}

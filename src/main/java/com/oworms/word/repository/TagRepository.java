package com.oworms.word.repository;

import com.oworms.word.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByNameIgnoreCase(String theWord);

    Optional<Tag> findByNameIgnoreCaseAndIdNot(String name, Long id);

}

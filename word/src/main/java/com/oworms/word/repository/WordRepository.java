package com.oworms.word.repository;

import com.oworms.word.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    Optional<Word> findByTheWordIgnoreCase(String theWord);

    Optional<Word> findByTheWordIgnoreCaseAndUuidNot(String theWord, String uuid);

    Optional<Word> findByUuid(String uuid);

    long countByTagsIdEquals(Long tagId);

}

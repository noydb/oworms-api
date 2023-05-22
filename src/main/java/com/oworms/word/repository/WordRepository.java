package com.oworms.word.repository;

import com.oworms.word.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    Optional<Word> findByTheWordIgnoreCase(final String theWord);

    Optional<Word> findByTheWordIgnoreCaseAndUuidNot(final String theWord, final String uuid);

    Optional<Word> findByUuid(final String uuid);

    long countByTagsIdEquals(final Long tagId);

    long countByCreatedByEquals(final String username);

    List<Word> findAllByCreatedBy(final String username);
}

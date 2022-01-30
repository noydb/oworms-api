package com.power.oworms.word.repository;

import com.power.oworms.word.domain.Word;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    Optional<Word> findByTheWordIgnoreCase(String theWord);

    Optional<Word> findByTheWordIgnoreCaseAndIdNot(String theWord, Long id);
}

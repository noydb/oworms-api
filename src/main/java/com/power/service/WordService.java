package com.power.service;

import com.power.domain.Word;
import com.power.dto.WordDTO;
import com.power.mapper.WordMapper;
import com.power.repository.WordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class WordService {

    private final WordRepository repository;
    private final WordMapper mapper;

    public WordService(final WordRepository repository,
                       final WordMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public void create(WordDTO wordDTO) {
        Word word = mapper.map(wordDTO);

        repository.save(word);
    }

    public List<WordDTO> retrieveAll() {
        List<Word> words = repository.findAll();

        return mapper.map(words);
    }
}

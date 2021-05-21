package com.power.service;

import com.power.domain.Word;
import com.power.dto.WordDTO;
import com.power.mapper.WordMapper;
import com.power.repository.WordRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class WordService {

    private final WordRepository repository;
    private final WordMapper mapper;
    private final FileService fileService;

    public WordService(final WordRepository repository,
                       final WordMapper mapper,
                       final FileService fileService) {
        this.repository = repository;
        this.mapper = mapper;
        this.fileService = fileService;
    }

    @Transactional
    public void create(final WordDTO wordDTO) {
        final Word word = mapper.map(wordDTO);

        repository.save(word);
    }

    public List<WordDTO> retrieveAll() {
        final List<Word> words = repository.findAll();

        return mapper.map(words);
    }

    public WordDTO retrieve(final String theWord) {
        final Word word = repository.findByTheWord(theWord).orElseThrow(EntityNotFoundException::new);

        word.setTimesViewed(word.getTimesViewed() + 1);
        repository.save(word);

        return mapper.map(word);
    }

    public boolean readCSV(final MultipartFile excelFile) {
        return fileService.writeWordsInSpreadsheetToDB(excelFile);
    }
}

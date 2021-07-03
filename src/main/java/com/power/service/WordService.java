package com.power.service;

import com.power.domain.PartOfSpeech;
import com.power.domain.Word;
import com.power.dto.WordDTO;
import com.power.mapper.WordMapper;
import com.power.repository.WordRepository;
import com.power.util.FilterUtil;
import com.power.util.WordUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Random;

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
    public void create(final WordDTO wordDTO, String id) {
        WordUtil.clean(wordDTO);

        if (wordExists(wordDTO)) {
            throw new EntityExistsException("That word already exists.");
        }

        final Word word = mapper.map(wordDTO, id);

        repository.save(word);
    }

    private boolean wordExists(WordDTO wordDTO) {
        return repository.findByTheWordIgnoreCase(wordDTO.getTheWord()).isPresent();
    }

    public List<WordDTO> retrieveAll(String theWord,
                                     String definition,
                                     List<String> partsOfSpeech,
                                     String creator,
                                     String haveLearnt) {
        final List<Word> words = repository.findAll();

        final List<Word> filteredWords = FilterUtil.filter(words, theWord, definition, partsOfSpeech, creator, haveLearnt);

        return mapper.map(filteredWords);
    }

    public WordDTO retrieve(final String theWord) {
        final Word word = repository.findByTheWordIgnoreCase(theWord).orElseThrow(EntityNotFoundException::new);

        word.setTimesViewed(word.getTimesViewed() + 1);
        repository.save(word);

        return mapper.map(word);
    }

    public WordDTO retrieveRandom() {
        final List<Word> words = repository.findAll();

        Random rand;
        int randomIndex = 0; // 0 (inclusive) -> arg (exclusive)
        try {
            rand = SecureRandom.getInstanceStrong();
            randomIndex = rand.nextInt(words.size());
        } catch (NoSuchAlgorithmException e) {
            // throw error.
        }

        Word randomWord = words.get(randomIndex);

        return mapper.map(randomWord);
    }

    public WordDTO update(String theWord, WordDTO updatedWord) {
        Word word = repository.findByTheWordIgnoreCase(theWord)
                .orElseThrow(() -> new EntityNotFoundException("that word don't exist friend."));

        boolean alreadyExists = repository.findByTheWordIgnoreCaseAndIdNot(updatedWord.getTheWord(), word.getId()).isPresent();
        if (alreadyExists) {
            throw new EntityExistsException("That word already exists.");
        }

        word.setTheWord(updatedWord.getTheWord());
        word.setDefinition(updatedWord.getDefinition());
        word.setPronunciation(updatedWord.getPronunciation());
        word.setOrigin(updatedWord.getOrigin());
        word.setPartOfSpeech(PartOfSpeech.getPartOfSpeech(updatedWord.getPartOfSpeech().getLabel()));
        word.setHaveLearnt(updatedWord.getHaveLearnt());
        // can't change createdBy and timesViewed

        word = repository.save(word);

        return mapper.map(word);
    }

    public boolean readCSV(final MultipartFile excelFile) {
        return fileService.writeWordsInSpreadsheetToDB(excelFile);
    }
}

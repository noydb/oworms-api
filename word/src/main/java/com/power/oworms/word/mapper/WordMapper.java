package com.power.oworms.word.mapper;

import com.power.oworms.mail.dto.EmailWordDTO;
import com.power.oworms.word.domain.PartOfSpeech;
import com.power.oworms.word.domain.Word;
import com.power.oworms.word.dto.WordDTO;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class WordMapper {

    private WordMapper() {
    }

    public static Word map(WordDTO wordDTO) {
        PartOfSpeech partOfSpeech = PartOfSpeech.getPartOfSpeech(wordDTO.getPartOfSpeech());

        Word word = new Word();

        word.setId(word.getId());
        word.setTheWord(wordDTO.getTheWord());
        word.setDefinition(wordDTO.getDefinition());
        word.setPartOfSpeech(partOfSpeech);
        word.setPronunciation(wordDTO.getPronunciation());
        word.setOrigin(wordDTO.getOrigin());
        word.setExampleUsage(wordDTO.getExampleUsage());
        word.setNote(wordDTO.getNote());
        word.setTags(TagMapper.mapFrom(wordDTO.getTags()));
        word.setCreationDate(wordDTO.getCreationDate());
        word.setCreatedBy(wordDTO.getCreatedBy());
        word.setTimesViewed(wordDTO.getTimesViewed());

        return word;
    }

    public static WordDTO map(Word word) {
        WordDTO wordDTO = new WordDTO();

        wordDTO.setId(word.getId());
        wordDTO.setTheWord(word.getTheWord());
        wordDTO.setDefinition(word.getDefinition());
        wordDTO.setPartOfSpeech(word.getPartOfSpeech().getLabel());
        wordDTO.setPronunciation(word.getPronunciation());
        wordDTO.setOrigin(word.getOrigin());
        wordDTO.setExampleUsage(word.getExampleUsage());
        wordDTO.setNote(word.getNote());
        wordDTO.setTags(TagMapper.mapTo(word.getTags()));
        wordDTO.setCreationDate(word.getCreationDate());
        wordDTO.setCreatedBy(word.getCreatedBy());
        wordDTO.setTimesViewed(word.getTimesViewed());

        return wordDTO;
    }

    public static List<WordDTO> mapTo(List<Word> words) {
        if (null == words) {
            return new ArrayList<>();
        }

        return words
                .stream()
                .map(WordMapper::map)
                .collect(toList());
    }

    public static List<Word> mapFrom(List<WordDTO> wordDTOs) {
        if (null == wordDTOs) {
            return new ArrayList<>();
        }

        return wordDTOs
                .stream()
                .map(WordMapper::map)
                .collect(toList());
    }

    public static EmailWordDTO mapToEmailDTO(WordDTO wordDTO) {
        EmailWordDTO emailWord = new EmailWordDTO();

        emailWord.setId(wordDTO.getId());
        emailWord.setTheWord(wordDTO.getTheWord());
        emailWord.setPartOfSpeech(wordDTO.getPartOfSpeech());
        emailWord.setDefinition(wordDTO.getDefinition());
        emailWord.setOrigin(wordDTO.getOrigin());
        emailWord.setExampleUsage(wordDTO.getOrigin());
        emailWord.setTags(TagMapper.getPretty(wordDTO.getTags()));
        emailWord.setNote(wordDTO.getNote());
        emailWord.setCreatedBy(wordDTO.getCreatedBy());

        return emailWord;
    }
}

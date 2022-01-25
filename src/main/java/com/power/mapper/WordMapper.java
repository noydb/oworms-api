package com.power.mapper;

import com.power.domain.PartOfSpeech;
import com.power.domain.Word;
import com.power.dto.WordDTO;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class WordMapper {

    private WordMapper() {
    }

    public static Word map(WordDTO wordDTO) {
        PartOfSpeech partOfSpeech = PartOfSpeech.getPartOfSpeech(wordDTO.getPartOfSpeech());

        return Word
                .builder()
                .theWord(wordDTO.getTheWord())
                .definition(wordDTO.getDefinition())
                .partOfSpeech(partOfSpeech)
                .pronunciation(wordDTO.getPronunciation())
                .origin(wordDTO.getOrigin())
                .exampleUsage(wordDTO.getExampleUsage())
                .note(wordDTO.getNote())
                .creationDate(wordDTO.getCreationDate())
                .createdBy(wordDTO.getCreatedBy())
                .timesViewed(wordDTO.getTimesViewed())
                .build();
    }

    public static WordDTO map(Word word) {
        return WordDTO
                .builder()
                .id(word.getId())
                .theWord(word.getTheWord())
                .definition(word.getDefinition())
                .partOfSpeech(word.getPartOfSpeech().getLabel())
                .pronunciation(word.getPronunciation())
                .origin(word.getOrigin())
                .exampleUsage(word.getExampleUsage())
                .note(word.getNote())
                .tags(TagMapper.mapTo(word.getTags()))
                .creationDate(word.getCreationDate())
                .createdBy(word.getCreatedBy())
                .timesViewed(word.getTimesViewed())
                .build();
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
}

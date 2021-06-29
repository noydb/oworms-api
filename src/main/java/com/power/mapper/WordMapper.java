package com.power.mapper;

import com.power.domain.PartOfSpeech;
import com.power.domain.Word;
import com.power.dto.PartOfSpeechDTO;
import com.power.dto.WordDTO;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
public class WordMapper {

    public Word map(WordDTO wordDTO, String id) {
        String createdBy = wordDTO.getCreatedBy();
        if (createdBy == null) {
            createdBy = id;
        }

        PartOfSpeech partOfSpeech = PartOfSpeech.valueOf(wordDTO.getPartOfSpeech().getLabel());

        return Word
                .builder()
                .theWord(wordDTO.getTheWord())
                .definition(wordDTO.getDefinition())
                .pronunciation(wordDTO.getPronunciation())
                .origin(wordDTO.getOrigin())
                .partOfSpeech(partOfSpeech)
                .haveLearnt(wordDTO.getHaveLearnt())
                .createdBy(createdBy)
                .build();
    }

    public WordDTO map(Word word) {
        return WordDTO
                .builder()
                .id(word.getId())
                .theWord(word.getTheWord())
                .definition(word.getDefinition())
                .pronunciation(word.getPronunciation())
                .origin(word.getOrigin())
                .partOfSpeech(PartOfSpeechDTO.getPartOfSpeech(word.getPartOfSpeech().getLabel()))
                .haveLearnt(word.getHaveLearnt())
                .createdBy(word.getCreatedBy())
                .timesViewed(word.getTimesViewed())
                .build();
    }

    public List<WordDTO> map(List<Word> words) {
        return words
                .parallelStream()
                .map(this::map)
                .collect(toList());
    }
}

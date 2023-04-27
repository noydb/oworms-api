package com.oworms.word.mapper;

import com.oworms.common.util.Utils;
import com.oworms.mail.dto.EmailWordDTO;
import com.oworms.mail.dto.UpdatedWordEmailDTO;
import com.oworms.word.domain.PartOfSpeech;
import com.oworms.word.domain.Word;
import com.oworms.word.dto.WordDTO;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class WordMapper {

    private WordMapper() {
    }

    public static Word map(final WordDTO wordDTO) {
        PartOfSpeech partOfSpeech = PartOfSpeech.getPartOfSpeech(wordDTO.getPartOfSpeech());

        Word word = new Word();

        word.setTheWord(wordDTO.getTheWord());
        word.setDefinition(wordDTO.getDefinition());
        word.setPartOfSpeech(partOfSpeech);
        word.setPronunciation(wordDTO.getPronunciation());
        word.setOrigin(wordDTO.getOrigin());
        word.setDiscoveredAt(wordDTO.getDiscoveredAt());
        word.setExampleUsage(wordDTO.getExampleUsage());
        word.setNote(wordDTO.getNote());
        word.setTags(TagMapper.mapFrom(wordDTO.getTags()));
        word.setCreationDate(wordDTO.getCreationDate());
        word.setCreatedBy(wordDTO.getCreatedBy());
        word.setTimesViewed(wordDTO.getTimesViewed());

        return word;
    }

    public static WordDTO map(final Word word) {
        final WordDTO wordDTO = new WordDTO();

        wordDTO.setUuid(word.getUuid());
        wordDTO.setTheWord(word.getTheWord());
        wordDTO.setDefinition(word.getDefinition());
        wordDTO.setPartOfSpeech(word.getPartOfSpeech().getLabel());
        wordDTO.setPronunciation(word.getPronunciation());
        wordDTO.setOrigin(word.getOrigin());
        wordDTO.setDiscoveredAt(word.getDiscoveredAt());
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

    public static EmailWordDTO mapToEmailDTO(final WordDTO wordDTO) {
        final EmailWordDTO emailWord = new EmailWordDTO();

        emailWord.setUuid(wordDTO.getUuid());
        emailWord.setTheWord(wordDTO.getTheWord());
        emailWord.setDefinition(wordDTO.getDefinition());
        emailWord.setPartOfSpeech(wordDTO.getPartOfSpeech());
        emailWord.setOrigin(valueElseNA(wordDTO.getOrigin()));
        emailWord.setDiscoveredAt(valueElseNA(wordDTO.getDiscoveredAt()));
        emailWord.setExampleUsage(valueElseNA(wordDTO.getExampleUsage()));
        emailWord.setPronunciation(valueElseNA(wordDTO.getPronunciation()));
        emailWord.setTags(valueElseNA(TagMapper.getPretty(wordDTO.getTags())));
        emailWord.setNote(valueElseNA(wordDTO.getNote()));
        emailWord.setCreatedBy(wordDTO.getCreatedBy());

        return emailWord;
    }

    private static String valueElseNA(String arg) {
        if (Utils.isBlank(arg)) {
            return "N/A";
        }

        return arg;
    }

    public static UpdatedWordEmailDTO mapToUpdateEmailDTO(final WordDTO old,
                                                          final WordDTO updated) {
        final UpdatedWordEmailDTO updatedWordEmailDTO = new UpdatedWordEmailDTO(
                "oworms | " + updated.getTheWord() + " updated",
                WordMapper.mapToEmailDTO(old),
                WordMapper.mapToEmailDTO(updated)
        );

        return updatedWordEmailDTO;
    }
}

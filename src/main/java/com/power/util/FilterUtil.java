package com.power.util;

import com.power.domain.PartOfSpeech;
import com.power.domain.Tag;
import com.power.domain.Word;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class FilterUtil {

    public static List<Word> filter(List<Word> words,
                                    String theWord,
                                    List<String> pos,
                                    String def,
                                    String origin,
                                    String example,
                                    List<String> tags,
                                    String note,
                                    String creator) {
        List<Word> filteredWords = words
                .parallelStream()
                .filter(word -> isAMatch(word.getTheWord(), theWord))
                .filter(word -> partOfSpeechMatch(word.getPartOfSpeech(), pos))
                .filter(word -> isAMatch(word.getDefinition(), def))
                .filter(word -> isAMatch(word.getOrigin(), origin))
                .filter(word -> isAMatch(word.getExampleUsage(), example))
                .filter(word -> tagsMatch(word.getTags(), tags))
                .filter(word -> isAMatch(word.getNote(), note))
                .filter(word -> isAMatch(word.getCreatedBy(), creator))
                .collect(toList());

        return filteredWords;
    }

    private static boolean tagsMatch(List<Tag> tags, List<String> searchTags) {
        if (null == searchTags) {
            return true;
        }

        for (String searchTag : searchTags) {
            for (Tag tag : tags) {
                if (WordUtil.isEqual(tag.getName(), searchTag)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isAMatch(String theWord, String filterWord) {
        if (WordUtil.isBlank(filterWord)) {
            return true;
        }

        return WordUtil.isEqual(theWord, filterWord);
    }

    private static boolean partOfSpeechMatch(PartOfSpeech partOfSpeech, List<String> partsOfSpeech) {
        if (null == partsOfSpeech || partsOfSpeech.isEmpty()) {
            return true;
        }

        return partsOfSpeech
                .stream()
                .anyMatch((posFilter) -> isAMatch(partOfSpeech, posFilter));
    }

    private static boolean isAMatch(PartOfSpeech partOfSpeech, String posFilter) {
        if (WordUtil.isBlank(posFilter)) {
            return true;
        }

        try {
            PartOfSpeech partOfSpeechParsed = PartOfSpeech.getPartOfSpeech(posFilter);

            return partOfSpeech.equals(partOfSpeechParsed);
        } catch (IllegalArgumentException e) {
            // the part of speech is not recognized. return true to ignore this filter.
            // (the user is dumb)
            return true;
        }
    }

}

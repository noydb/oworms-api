package com.oworms.word.util;

import com.oworms.common.util.Utils;
import com.oworms.word.domain.PartOfSpeech;
import com.oworms.word.domain.Tag;
import com.oworms.word.domain.Word;

import java.util.Comparator;
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
                .filter(word ->
                        isAMatch(word.getTheWord(), theWord) || partOfSpeechMatch(word.getPartOfSpeech(), pos)
                                || isAMatch(word.getDefinition(), def) || isAMatch(word.getOrigin(), origin)
                                || isAMatch(word.getExampleUsage(), example) || tagsMatch(word.getTags(), tags)
                                || isAMatch(word.getNote(), note) || isAMatch(word.getCreatedBy(), creator)
                )
                .sorted(Comparator.comparing(Word::getTheWord))
                .collect(toList());

        return filteredWords;
    }

    private static boolean tagsMatch(List<Tag> tags, List<String> searchTags) {
        if (null == searchTags) {
            return true;
        }

        for (String searchTag : searchTags) {
            for (Tag tag : tags) {
                if (Utils.areEqual(tag.getName(), searchTag)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isAMatch(String theWord, String filterWord) {
        if (Utils.isBlank(filterWord)) {
            return true;
        }

        return Utils.areEqual(theWord, filterWord);
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
        if (Utils.isBlank(posFilter)) {
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

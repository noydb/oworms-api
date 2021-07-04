package com.power.util;

import com.power.domain.PartOfSpeech;
import com.power.domain.Word;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class FilterUtil {

    public static List<Word> filter(List<Word> words,
                                    String theWord,
                                    String definition,
                                    List<String> partsOfSpeech,
                                    String creator,
                                    String haveLearnt) {
        List<Word> filteredWords = words
                .parallelStream()
                .filter(word -> isAMatch(word.getTheWord(), theWord))
                .filter(word -> isAMatch(word.getDefinition(), definition))
                .filter(word -> partOfSpeechMatch(word.getPartOfSpeech(), partsOfSpeech))
                .filter(word -> isAMatch(word.getCreatedBy(), creator))
                .filter(word -> haveLearntMatch(word.isHaveLearnt(), haveLearnt))
                .collect(toList());

        return filteredWords;
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

    private static boolean haveLearntMatch(boolean haveLearnt, String hlFilter) {
        boolean invalidFilter = hlFilter == null || !WordUtil.isEqual(hlFilter, "y") || !WordUtil.isEqual(hlFilter, "n");

        if (invalidFilter) {
            return true;
        }

        if (hlFilter.equals("y")) {
            return haveLearnt;
        }

        return !haveLearnt;
    }

}

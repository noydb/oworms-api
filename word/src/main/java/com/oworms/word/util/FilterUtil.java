package com.oworms.word.util;

import com.oworms.common.util.Utils;
import com.oworms.word.domain.PartOfSpeech;
import com.oworms.word.domain.Tag;
import com.oworms.word.domain.Word;
import com.oworms.word.dto.WordFilter;

import java.util.Comparator;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class FilterUtil {

    public static List<Word> filter(final List<Word> words, final WordFilter wordFilter) {
        List<Word> filteredWords = words
                .parallelStream()
                .filter(word -> isAMatch(word.getTheWord(), wordFilter.getWord()))
                .filter(word -> partOfSpeechMatch(word.getPartOfSpeech(), wordFilter.getPos()))
                .filter(word -> isAMatch(word.getDefinition(), wordFilter.getDef()))
                .filter(word -> isAMatch(word.getOrigin(), wordFilter.getOrigin()))
                .filter(word -> isAMatch(word.getExampleUsage(), wordFilter.getExample()))
                .filter(word -> tagsMatch(word.getTags(), wordFilter.getTags()))
                .filter(word -> isAMatch(word.getNote(), wordFilter.getNote()))
                .filter(word -> isAMatch(word.getCreatedBy(), wordFilter.getCreator()))
                .filter(word -> uuidMatch(word.getUuid(), wordFilter.getUuids()))
                .sorted(Comparator.comparing(Word::getTheWord))
                .collect(toList());

        return filteredWords;
    }

    private static boolean uuidMatch(String uuid, List<String> uuids) {
        if (uuids == null || uuids.isEmpty()) {
            return true;
        }

        return uuids.contains(uuid);
    }

    private static boolean tagsMatch(final List<Tag> tags, final List<String> searchTags) {
        if (null == searchTags) {
            return true;
        }

        for (final String searchTag : searchTags) {
            for (final Tag tag : tags) {
                if (Utils.areEqual(tag.getName(), searchTag)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static boolean isAMatch(final String theWord, final String filterWord) {
        if (Utils.isBlank(filterWord)) {
            return true;
        }

        return Utils.areEqual(theWord, filterWord);
    }

    private static boolean partOfSpeechMatch(final PartOfSpeech partOfSpeech, final List<String> partsOfSpeech) {
        if (null == partsOfSpeech || partsOfSpeech.isEmpty()) {
            return true;
        }

        return partsOfSpeech
                .stream()
                .anyMatch((posFilter) -> isAMatch(partOfSpeech, posFilter));
    }

    private static boolean isAMatch(final PartOfSpeech partOfSpeech, final String posFilter) {
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

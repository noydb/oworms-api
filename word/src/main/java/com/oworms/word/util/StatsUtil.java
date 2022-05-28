package com.oworms.word.util;

import com.oworms.word.domain.PartOfSpeech;
import com.oworms.word.domain.Word;
import com.oworms.word.dto.StatisticsDTO;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class StatsUtil {

    private static final List<String> LETTERS = Arrays.asList(
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
    );

    private StatsUtil() {
    }

    public static void getPartsOfSpeechStats(StatisticsDTO stats, List<Word> words) {
        Map<String, Integer> partsOfSpeechTotals = new HashMap<>();
        Map<String, String> partsOfSpeechPercentages = new HashMap<>();

        for (PartOfSpeech partOfSpeech : PartOfSpeech.values()) {
            List<Word> wordsThatAreThisPOS = words
                    .parallelStream()
                    .filter(wordComparator -> wordComparator.getPartOfSpeech().equals(partOfSpeech))
                    .collect(toList());

            String posLabel = partOfSpeech.getLabel();
            partsOfSpeechTotals.put(posLabel, wordsThatAreThisPOS.size());
            partsOfSpeechPercentages.put(posLabel, StatsUtil.getPercentagePretty(wordsThatAreThisPOS.size(), words.size()));
        }

        stats.setPartsOfSpeechTotals(partsOfSpeechTotals);
        stats.setPartsOfSpeechPercentages(partsOfSpeechPercentages);
    }

    public static void getFirstLetterStats(final StatisticsDTO stats, final List<Word> words) {
        Map<String, Integer> firstLetterTotals = new HashMap<>(LETTERS.size());
        Map<String, String> firstLetterPercentages = new HashMap<>(LETTERS.size());

        LETTERS.forEach(letter -> {
            List<Word> wordsStartingWithLetter = words
                    .parallelStream()
                    .filter(word -> word.getTheWord().toLowerCase(Locale.ROOT).startsWith(letter))
                    .collect(toList());

            firstLetterTotals.put(letter, wordsStartingWithLetter.size());
            firstLetterPercentages.put(letter, getPercentagePretty(wordsStartingWithLetter.size(), words.size()));
        });

        stats.setFirstLetterTotals(firstLetterTotals);
        stats.setFirstLetterPercentages(firstLetterPercentages);
    }

    private static String getPercentagePretty(final Integer amount, final Integer total) {
        final double percentage = Double.valueOf(amount) / Double.valueOf(total) * 100d;

        final double rounded = Math.round(percentage * 100) / 100.00;

        return rounded + "%";
    }

}

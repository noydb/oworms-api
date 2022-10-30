package com.oworms.word.util;

import com.oworms.word.domain.PartOfSpeech;
import com.oworms.word.domain.Word;
import com.oworms.word.dto.StatisticsDTO;
import com.oworms.word.dto.WordDTO;
import com.oworms.word.mapper.WordMapper;
import org.springframework.validation.annotation.Validated;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

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

    public static void createDateWordMap(final StatisticsDTO stats, final List<Word> words) {
        final Map<String, Set<WordDTO>> dateWordMap = new HashMap<>();

        for (final Word word : words) {
            OffsetDateTime offsetDateTime = word.getCreationDate();

            String key = offsetDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE);

            Set<WordDTO> currentWordsForDate = dateWordMap.get(key);
            if (currentWordsForDate == null) {
                currentWordsForDate = new HashSet<>(Collections.singletonList(WordMapper.map(word)));
            } else {
                currentWordsForDate.add(WordMapper.map(word));
            }
            dateWordMap.put(key, currentWordsForDate);

//            switch (type) {
//                case DAY:
//                    int dayOfMonth = offsetDateTime.getDayOfMonth();
//                    if (dayOfMonth == dateFilter) {
//                        dateOrDateTimeMap.put(dayOfMonth, word.getId());
//                    }
//
//                case MONTH:
//                    int monthOfYear = offsetDateTime.getMonthValue();
//                    if (monthOfYear == dateFilter) {
//                        dateOrDateTimeMap.put(dayOfMonth, word.getId());
//                    }
//
//            }
        }

        stats.setDateOrDateTimeMap(dateWordMap);
    }

    @Validated
    class DateStatFilter {
        int day;
        int month;
        int year;
    }
}

package com.power.service;

import com.power.domain.PartOfSpeech;
import com.power.domain.Word;
import com.power.dto.StatisticsDTO;
import com.power.repository.WordRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static java.util.stream.Collectors.toList;

@Service
public class SettingsService {

    private final WordRepository repository;

    public SettingsService(final WordRepository repository) {
        this.repository = repository;
    }

    public StatisticsDTO getStatistics() {
        StatisticsDTO.StatisticsDTOBuilder builder = StatisticsDTO.builder();

        List<Word> words = repository.findAll();

        int totalWords = (int) repository.count();
        builder.totalWords(totalWords);

        int totalViewsOnWords = words
                .parallelStream()
                .reduce(0, (acc, current) -> acc + current.getTimesViewed(), Integer::sum);
        builder.totalViewsOnWords(totalViewsOnWords);

        getPartsOfSpeechStats(builder, words);
        getFirstLetterStats(builder, words);

        return builder.build();
    }

    private void getPartsOfSpeechStats(StatisticsDTO.StatisticsDTOBuilder builder, List<Word> words) {
        Map<String, Integer> partsOfSpeechTotals = new HashMap<>();
        Map<String, String> partsOfSpeechPercentages = new HashMap<>();

        for (PartOfSpeech partOfSpeech : PartOfSpeech.values()) {
            List<Word> wordsThatAreThisPOS = words
                    .parallelStream()
                    .filter(wordComparator -> wordComparator.getPartOfSpeech().equals(partOfSpeech))
                    .collect(toList());

            String posLabel = partOfSpeech.getLabel();
            partsOfSpeechTotals.put(posLabel, wordsThatAreThisPOS.size());
            partsOfSpeechPercentages.put(posLabel, getPercentagePretty(wordsThatAreThisPOS.size(), words.size()));
        }

        builder.partsOfSpeechTotals(partsOfSpeechTotals);
        builder.partsOfSpeechPercentages(partsOfSpeechPercentages);
    }

    private void getFirstLetterStats(final StatisticsDTO.StatisticsDTOBuilder builder, final List<Word> words) {
        final List<String> letters = Arrays.asList(
                "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
                "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"
        );

        Map<String, Integer> firstLetterTotals = new HashMap<>(letters.size());
        Map<String, String> firstLetterPercentages = new HashMap<>(letters.size());

        letters.forEach(letter -> {
            List<Word> wordsStartingWithLetter = words
                    .parallelStream()
                    .filter(word -> word.getTheWord().toLowerCase(Locale.ROOT).startsWith(letter))
                    .collect(toList());

            firstLetterTotals.put(letter, wordsStartingWithLetter.size());
            firstLetterPercentages.put(letter, getPercentagePretty(wordsStartingWithLetter.size(), words.size()));
        });

        builder.firstLetterTotals(firstLetterTotals);
        builder.firstLetterPercentages(firstLetterPercentages);
    }

    private String getPercentagePretty(final Integer amount, final Integer total) {
        final double percentage = Double.valueOf(amount) / Double.valueOf(total) * 100d;

        final double rounded = Math.round(percentage * 100) / 100.00;

        return rounded + "%";
    }
}

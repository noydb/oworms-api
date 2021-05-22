package com.power.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
@Setter
public class StatisticsDTO {
    private long totalWords;
    private int totalViewsOnWords;
    private int numberOfWordsLearnt;
    private String percentageOfWordsLearnt;
    private Map<String, Integer> partsOfSpeechTotals;
    private Map<String, String> partsOfSpeechPercentages;
    private Map<String, Integer> firstLetterTotals;
    private Map<String, String> firstLetterPercentages;
}

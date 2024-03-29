package com.oworms.word.dto;

import java.util.Map;
import java.util.Set;

public class StatisticsDTO {
    private long totalWords;
    private int totalViewsOnWords;
    private String percentageOfWordsLearnt;
    private Map<String, Integer> partsOfSpeechTotals;
    private Map<String, String> partsOfSpeechPercentages;
    private Map<String, Integer> firstLetterTotals;
    private Map<String, String> firstLetterPercentages;
    private Map<String, Set<WordDTO>> dateWordMap;

    public long getTotalWords() {
        return totalWords;
    }

    public void setTotalWords(long totalWords) {
        this.totalWords = totalWords;
    }

    public int getTotalViewsOnWords() {
        return totalViewsOnWords;
    }

    public void setTotalViewsOnWords(int totalViewsOnWords) {
        this.totalViewsOnWords = totalViewsOnWords;
    }

    public String getPercentageOfWordsLearnt() {
        return percentageOfWordsLearnt;
    }

    public void setPercentageOfWordsLearnt(String percentageOfWordsLearnt) {
        this.percentageOfWordsLearnt = percentageOfWordsLearnt;
    }

    public Map<String, Integer> getPartsOfSpeechTotals() {
        return partsOfSpeechTotals;
    }

    public void setPartsOfSpeechTotals(Map<String, Integer> partsOfSpeechTotals) {
        this.partsOfSpeechTotals = partsOfSpeechTotals;
    }

    public Map<String, String> getPartsOfSpeechPercentages() {
        return partsOfSpeechPercentages;
    }

    public void setPartsOfSpeechPercentages(Map<String, String> partsOfSpeechPercentages) {
        this.partsOfSpeechPercentages = partsOfSpeechPercentages;
    }

    public Map<String, Integer> getFirstLetterTotals() {
        return firstLetterTotals;
    }

    public void setFirstLetterTotals(Map<String, Integer> firstLetterTotals) {
        this.firstLetterTotals = firstLetterTotals;
    }

    public Map<String, String> getFirstLetterPercentages() {
        return firstLetterPercentages;
    }

    public void setFirstLetterPercentages(Map<String, String> firstLetterPercentages) {
        this.firstLetterPercentages = firstLetterPercentages;
    }

    public Map<String, Set<WordDTO>> getDateWordMap() {
        return dateWordMap;
    }

    public void setDateOrDateTimeMap(Map<String, Set<WordDTO>> dateWordMap) {
        this.dateWordMap = dateWordMap;
    }
}

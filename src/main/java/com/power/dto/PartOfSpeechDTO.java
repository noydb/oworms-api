package com.power.dto;

public enum PartOfSpeechDTO {

    ADJECTIVE("Adjective"),
    ADVERB("Adverb"),
    NOUN("Noun"),
    OTHER("Other"),
    VERB("Verb");

    private final String label;

    PartOfSpeechDTO(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static PartOfSpeechDTO getPartOfSpeech(String arg) {
        if (null == arg) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        for (PartOfSpeechDTO value : PartOfSpeechDTO.values()) {
            if (arg.equals(value.getLabel())) {
                return value;
            }
        }

        throw new IllegalArgumentException("That part of speech does not exist");
    }
}

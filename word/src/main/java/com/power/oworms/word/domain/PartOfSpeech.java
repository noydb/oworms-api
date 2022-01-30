package com.power.oworms.word.domain;

public enum PartOfSpeech {

    ADJECTIVE("Adjective"),
    ADVERB("Adverb"),
    NOUN("Noun"),
    OTHER("Other"),
    VERB("Verb");

    private final String label;

    PartOfSpeech(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public static PartOfSpeech getPartOfSpeech(String arg) {
        if (null == arg) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        for (PartOfSpeech value : PartOfSpeech.values()) {
            if (arg.equalsIgnoreCase(value.getLabel())) {
                return value;
            }
        }

        throw new IllegalArgumentException("That part of speech does not exist");
    }
}

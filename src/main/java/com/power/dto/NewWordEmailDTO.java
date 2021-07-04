package com.power.dto;

import lombok.Data;

@Data
public class NewWordEmailDTO {

    public static final String SUBJECT = "oworms | new word added";
    public static final String TEMPLATE = "new-word";
    private String[] recipients;
    private String word;
    private String definition;
    private String partOfSpeech;
    private String retrievalLink;
}

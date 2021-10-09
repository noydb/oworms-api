package com.power.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NewWordEmailDTO {

    public static final String TEMPLATE = "oworm-new";
    public static final String ACTION = "A new word was recently added";
    private String title;
    private WordDTO word;
    private String retrievalLink;

    public NewWordEmailDTO(String title, WordDTO word, String retrievalLink) {
        this.title = title;
        this.word = word;
        this.retrievalLink = retrievalLink;
    }
}

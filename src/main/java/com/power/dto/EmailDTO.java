package com.power.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailDTO {

    public static final String TEMPLATE = "oworm-action";

    private String title;
    private String action;
    private WordDTO word;
    private String retrievalLink;

    public EmailDTO(String title, String action, WordDTO word, String retrievalLink) {
        this.title = title;
        this.action = action;
        this.word = word;
        this.retrievalLink = retrievalLink;
    }
}

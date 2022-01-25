package com.power.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class NewWordEmailDTO {

    public static final String TEMPLATE = "oworm-new";
    public static final String ACTION = "A new word was recently added";
    private String title;
    private WordDTO word;
    private String tags;
    private String retrievalLink;

    public NewWordEmailDTO(String title, WordDTO word, String retrievalLink) {
        this.title = title;
        this.word = word;
        this.retrievalLink = retrievalLink;

        setTags(word.getTags());
    }

    private void setTags(List<TagDTO> tags) {
        if (null == tags || tags.isEmpty()) {
            this.tags = "N/A";
            return;
        }

        this.tags = tags
                .stream()
                .map(TagDTO::getName)
                .collect(Collectors.joining(", "));
    }
}

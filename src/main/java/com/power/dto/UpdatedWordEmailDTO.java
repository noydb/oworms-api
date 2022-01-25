package com.power.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
public class UpdatedWordEmailDTO {

    public static final String TEMPLATE = "oworm-update";
    public static final String ACTION = "A word was recently updated";
    private String title;
    private WordDTO oldWord;
    private WordDTO updatedWord;
    private String oldTags;
    private String newTags;
    private String retrievalLink;

    public UpdatedWordEmailDTO(String title, WordDTO oldWord, WordDTO updatedWord, String retrievalLink) {
        this.title = title;
        this.oldWord = oldWord;
        this.updatedWord = updatedWord;
        this.retrievalLink = retrievalLink;

        this.oldTags = getTagsPretty(oldWord.getTags());
        this.newTags = getTagsPretty(updatedWord.getTags());
    }

    private String getTagsPretty(List<TagDTO> tags) {
        if (null == tags || tags.isEmpty()) {
            return "N/A";
        }

        return tags
                .stream()
                .map(TagDTO::getName)
                .collect(Collectors.joining(", "));
    }
}

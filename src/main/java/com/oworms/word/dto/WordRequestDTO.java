package com.oworms.word.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Valid
public class WordRequestDTO {

    @NotNull
    @Valid
    private WordDTO word;

    private List<Long> tagIds;

    public WordDTO getWord() {
        return word;
    }

    public void setWord(WordDTO word) {
        this.word = word;
    }

    public List<Long> getTagIds() {
        return tagIds;
    }

    public void setTagIds(List<Long> tagIds) {
        this.tagIds = tagIds;
    }
}

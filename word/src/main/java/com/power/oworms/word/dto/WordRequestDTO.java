package com.power.oworms.word.dto;

import com.power.oworms.word.dto.WordDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Valid
public class WordRequestDTO {

    @NotNull
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

package com.power.oworms.word.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Valid
public class TagDTO {

    private Long id;

    @NotEmpty
    @Size(max = 30, message = "Name cannot be more than 30 characters long")
    private String name;

    private int wordCount;

    private List<WordDTO> words;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<WordDTO> getWords() {
        return words;
    }

    public void setWords(List<WordDTO> words) {
        this.words = words;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }
}

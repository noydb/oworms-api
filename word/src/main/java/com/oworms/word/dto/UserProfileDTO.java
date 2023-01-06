package com.oworms.word.dto;

import java.util.List;

public class UserProfileDTO {

    private int createdWordCount;
    private List<WordDTO> likedWords;

    public UserProfileDTO(int createdWordCount, List<WordDTO> likedWords) {
        this.createdWordCount = createdWordCount;
        this.likedWords = likedWords;
    }

    public long getCreatedWordCount() {
        return createdWordCount;
    }

    public void setCreatedWordCount(int createdWordCount) {
        this.createdWordCount = createdWordCount;
    }

    public List<WordDTO> getLikedWords() {
        return likedWords;
    }

    public void setLikedWords(List<WordDTO> likedWords) {
        this.likedWords = likedWords;
    }
}

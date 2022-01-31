package com.power.oworms.word.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;

@Valid
public class WordDTO {

    private Long id;

    @NotEmpty(message = "Word is required")
    @Size(max = 50, message = "Word cannot be more than 50 characters long")
    private String theWord;

    @NotEmpty(message = "Definition is required")
    @Size(max = 2000, message = "Definition cannot be more than 2000 characters long")
    private String definition;

    @NotEmpty(message = "Part of speech is required")
    @Size(max = 50, message = "Part of speech cannot be more than 50 characters long")
    private String partOfSpeech;

    @Size(min = 1, max = 20, message = "Pronunciation cannot be more than 20 characters long")
    private String pronunciation;

    @Size(max = 2000, message = "Origin cannot be more than 2000 characters long")
    private String origin;

    @Size(max = 2000, message = "Example usage cannot be more than 2000 characters long")
    private String exampleUsage;

    @Size(max = 2000, message = "Note cannot be more than 2000 characters long")
    private String note;

    private List<TagDTO> tags;

    private OffsetDateTime creationDate;

    @Size(max = 15, message = "Created by cannot be more than 15 characters long")
    private String createdBy;

    private int timesViewed = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTheWord() {
        return theWord;
    }

    public void setTheWord(String theWord) {
        this.theWord = theWord;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(String partOfSpeech) {
        this.partOfSpeech = partOfSpeech;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public void setPronunciation(String pronunciation) {
        this.pronunciation = pronunciation;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getExampleUsage() {
        return exampleUsage;
    }

    public void setExampleUsage(String exampleUsage) {
        this.exampleUsage = exampleUsage;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public List<TagDTO> getTags() {
        return tags;
    }

    public void setTags(List<TagDTO> tags) {
        this.tags = tags;
    }

    public OffsetDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(OffsetDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public int getTimesViewed() {
        return timesViewed;
    }

    public void setTimesViewed(int timesViewed) {
        this.timesViewed = timesViewed;
    }
}

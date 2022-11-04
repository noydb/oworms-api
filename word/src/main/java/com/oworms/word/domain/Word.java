package com.oworms.word.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "word")
public class Word {

    @Id
    @GeneratedValue
    private Long id;

    @Column(length = 36, nullable = false, updatable = false)
    private String uuid;

    @Column(nullable = false, length = 50)
    private String theWord;

    @Column(nullable = false, length = 2000)
    private String definition;

    @Column(nullable = false, length = 50)
    private PartOfSpeech partOfSpeech;

    @Column(length = 50)
    private String pronunciation;

    @Column(length = 3500)
    private String origin;

    @Column(length = 3500)
    private String exampleUsage;

    @Column(length = 3500)
    private String note;

    @ManyToMany
    private List<Tag> tags;

    @Column(nullable = false, updatable = false)
    private OffsetDateTime creationDate;

    @Column(nullable = false, length = 80)
    private String createdBy;

    @Column(nullable = false)
    private Integer timesViewed;

    public Word() {
        uuid = UUID.randomUUID().toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

    public PartOfSpeech getPartOfSpeech() {
        return partOfSpeech;
    }

    public void setPartOfSpeech(PartOfSpeech partOfSpeech) {
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

    public List<Tag> getTags() {
        if (this.tags == null) {
            this.tags = new ArrayList<>();
        }

        return tags;
    }

    public void setTags(List<Tag> tags) {
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

    public Integer getTimesViewed() {
        return timesViewed;
    }

    public void setTimesViewed(Integer timesViewed) {
        this.timesViewed = timesViewed;
    }
}

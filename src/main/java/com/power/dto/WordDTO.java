package com.power.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Valid
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WordDTO implements Trim {

    private Long id;

    @NotEmpty
    private String theWord;

    @NotEmpty
    private String definition;

    private String pronunciation;

    private String origin;

    @NotEmpty
    private PartOfSpeechDTO partOfSpeech;

    @NotNull
    private Boolean haveLearnt;

    private String createdBy;

    private int timesViewed = 0;

    @Override
    public void trim() {
        if (null != theWord) {
            setTheWord(theWord.trim());
        }

        if (null != definition) {
            setDefinition(definition.trim());
        }

        if (null != pronunciation) {
            setPronunciation(pronunciation.trim());
        }

        if (null != origin) {
            setOrigin(origin.trim());
        }

        if (null != createdBy) {
            setCreatedBy(createdBy.trim());
        }
    }
}

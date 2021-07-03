package com.power.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

@Valid
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WordDTO {

    private Long id;

    @NotEmpty
    private String theWord;

    @NotEmpty
    private String definition;

    @NotEmpty
    private PartOfSpeechDTO partOfSpeech;

    private String pronunciation;

    private String origin;

    private String exampleUsage;

    private Boolean haveLearnt;
    private String createdBy;
    private int timesViewed = 0;
}

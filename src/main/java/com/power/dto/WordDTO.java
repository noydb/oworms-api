package com.power.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;

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
    private String partOfSpeech;

    private String pronunciation;

    private String origin;

    private String exampleUsage;

    private LocalDate creationDate;

    private boolean haveLearnt = false;

    private String createdBy;

    private int timesViewed = 0;
}

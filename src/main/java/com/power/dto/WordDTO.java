package com.power.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WordDTO {
    private Long id;
    private String theWord;
    private String definition;
    private String pronunciation;
    private String origin;
    private String partOfSpeech;
    private Boolean haveLearnt;

    private String createdBy;
    private int timesViewed;
}

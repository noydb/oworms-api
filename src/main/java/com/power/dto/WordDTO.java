package com.power.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;

@Valid
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WordDTO {

    private Long id;

    @NotEmpty(message = "Word is required")
    @Size(max = 50)
    private String theWord;

    @NotEmpty(message = "Definition is required")
    @Size(max = 2000)
    private String definition;

    @NotEmpty(message = "Part of speech is required")
    @Size(max = 50)
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

    private LocalDateTime creationDate;

    @Size(max = 15, message = "Created by cannot be more than 15 characters long")
    private String createdBy;

    private int timesViewed = 0;

}

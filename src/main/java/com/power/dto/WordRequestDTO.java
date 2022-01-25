package com.power.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Valid
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WordRequestDTO {

    @NotNull
    private WordDTO word;

    private List<Long> tagIds;
}

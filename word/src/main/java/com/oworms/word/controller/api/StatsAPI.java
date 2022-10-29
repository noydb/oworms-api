package com.oworms.word.controller.api;

import com.oworms.word.dto.StatisticsDTO;
import com.oworms.word.dto.WordDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface StatsAPI {

    @ApiOperation(
            value = "Retrieves statistics on the words in the database.",
            response = WordDTO.class,
            responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Retrieved statistics successfully"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error while retrieving statistics"
            )
    })
    ResponseEntity<StatisticsDTO> get();
}

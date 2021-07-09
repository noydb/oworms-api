package com.power.api;

import com.power.dto.StatisticsDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface SettingsAPI {

    @ApiOperation(
            value = "Retrieves the statistics of the application",
            response = StatisticsDTO.class
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Retrieved statistics"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Internal error while retrieving statistics"
            )
    })
    ResponseEntity<StatisticsDTO> getStatistics();
}

package com.power.oworms.word.controller.api;

import com.power.oworms.common.error.OWormException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

public interface FileAPI {

    @ApiOperation(
            code = 201,
            value = "Creates a CSV file containing all the words in the database and returns it"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Retrieved word CSV file successfully"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Internal error while retrieving the words"
            )
    })
    ResponseEntity<Resource> getCSV() throws OWormException;

}

package com.power.api;

import com.power.error.OWormException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface FileAPI {

    @ApiOperation(
            value = "Reads all words from the provided spreadsheet and persists them to the database. " +
                    "If a word already exists, it will simply be skipped."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Created all the words successfully"
            ),
            @ApiResponse(
                    code = 400,
                    message = "Supplied file is invalid"
            ),
            @ApiResponse(
                    code = 403,
                    message = "You do not have permission to do that"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Internal error while creating the words"
            )
    })
    ResponseEntity<Void> readCSV(MultipartFile excelFile, String permissionKey) throws OWormException;

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

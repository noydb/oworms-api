package com.power.oworms.auth.controller.api;

import com.power.oworms.auth.dto.NewUserDTO;
import com.power.oworms.auth.error.AccountExistsException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

public interface UserAPI {

    @ApiOperation(
            code = 201,
            value = "Creates a new user"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Created the user"
            ),
            @ApiResponse(
                    code = 400,
                    message = "Request input values are incorrect"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error while creating user"
            )
    })
    ResponseEntity<Void> create(NewUserDTO newUserDTO, String username, String banana) throws AccountExistsException;
}

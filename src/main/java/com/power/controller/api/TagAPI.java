package com.power.controller.api;

import com.power.dto.TagDTO;
import com.power.error.OWormException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface TagAPI {

    @ApiOperation(
            code = 201,
            value = "Creates a tag",
            response = TagDTO.class
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Created the tag successfully"
            ),
            @ApiResponse(
                    code = 400,
                    message = "Supplied values for creation are invalid"
            ),
            @ApiResponse(
                    code = 403,
                    message = "You do not have permission to do that"
            ),
            @ApiResponse(
                    code = 409,
                    message = "That tag already exists"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error while creating the tag"
            )
    })
    ResponseEntity<TagDTO> create(TagDTO tagDTO, String permissionKey) throws OWormException;

    @ApiOperation(
            value = "Retrieves all the tags meeting the criteria provided by the query parameters. " +
                    "If no query parameters are specified, all tags will be returned.",
            response = TagDTO.class,
            responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Retrieved all tags"
            ),
            @ApiResponse(
                    code = 404,
                    message = "No tags found"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error while retrieving all the tags"
            )
    })
    ResponseEntity<List<TagDTO>> retrieveAll(String name, String theWord);

    @ApiOperation(
            value = "Retrieves a single tag using the specified ID",
            response = TagDTO.class
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Retrieved the tag"
            ),
            @ApiResponse(
                    code = 404,
                    message = "That tag does not exist"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error while retrieving the tag"
            )
    })
    ResponseEntity<TagDTO> retrieve(Long tagId);

    @ApiOperation(
            code = 200,
            value = "Updates a tag",
            response = TagDTO.class
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Updated the tag successfully"
            ),
            @ApiResponse(
                    code = 400,
                    message = "Supplied values for update are invalid"
            ),
            @ApiResponse(
                    code = 403,
                    message = "You do not have permission to do that"
            ),
            @ApiResponse(
                    code = 409,
                    message = "That tag already exists"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error while updating the tag"
            )
    })
    ResponseEntity<TagDTO> update(Long tagId, String permissionKey, TagDTO updatedTagDTO) throws OWormException;

}

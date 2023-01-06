package com.oworms.word.controller.api;

import com.oworms.common.error.OWormException;
import com.oworms.word.dto.UserProfileDTO;
import com.oworms.word.dto.WordDTO;
import com.oworms.word.dto.WordRequestDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface WordAPI {

    @ApiOperation(
            code = 201,
            value = "Creates a word. Sends an email communication to relevant users upon successful creation",
            response = WordDTO.class
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Created the word successfully"
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
                    message = "That word already exists"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Error while sending email communicating the creation of the new word"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error while creating the word"
            )
    })
    ResponseEntity<WordDTO> create(WordRequestDTO wordDTO, String u, String banana) throws OWormException;

    @ApiOperation(
            value = "Retrieves all words meeting the criteria provided by the query parameters. " +
                    "If no query parameters are specified, all words will be returned.",
            response = WordDTO.class,
            responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Retrieved all words"
            ),
            @ApiResponse(
                    code = 404,
                    message = "No words found"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error while retrieving all the words"
            )
    })
    ResponseEntity<List<WordDTO>> retrieveAll(
            final int numberOfWords,
            final String theWord,
            final List<String> parts,
            final String def,
            final String origin,
            final String example,
            final List<String> tags,
            final String note,
            final String creator,
            final List<String> uuids
    );

    @ApiOperation(
            value = "Retrieves a single word using the specified uuid",
            response = WordDTO.class
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Retrieved the word"
            ),
            @ApiResponse(
                    code = 404,
                    message = "That word does not exist"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error while retrieving the word"
            )
    })
    ResponseEntity<WordDTO> retrieve(String uuid);

    @ApiOperation(
            value = "Retrieves a random word",
            response = WordDTO.class
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Retrieved the random word"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error while retrieving the random word"
            )
    })
    ResponseEntity<WordDTO> retrieveRandom();

    @ApiOperation(
            code = 200,
            value = "Updates a word. Sends an email communication to relevant users upon successful update",
            response = WordDTO.class
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 201,
                    message = "Updated the word successfully"
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
                    message = "That word already exists"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Error while sending email communicating the update of the word"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error while updating the word"
            )
    })
    ResponseEntity<WordDTO> update(String uuid, WordRequestDTO wordRequestDTO, String u, String banana) throws OWormException;

    @ApiOperation(
            code = 200,
            value = "Retrieves a user's profile data (which is number of words created and liked words)",
            response = UserProfileDTO.class
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Successfully retrieved the user's profile"
            ),
            @ApiResponse(
                    code = 403,
                    message = "You do not have permission to do that"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Internal server error while retrieving the user's profile"
            )
    })
    ResponseEntity<UserProfileDTO> getUserProfile(final String u, final String banana) throws OWormException;

}

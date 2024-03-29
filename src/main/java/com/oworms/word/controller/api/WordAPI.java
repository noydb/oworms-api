package com.oworms.word.controller.api;

import com.oworms.error.OWormException;
import com.oworms.word.dto.WordDTO;
import com.oworms.word.dto.WordRequestDTO;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
            value = "Retrieves all words meeting the criteria provided by the query parameters. Most query " +
                    "params will be used in a contains search. If no query parameters are specified, all " +
                    "words will be returned.",
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
            final @ApiParam(value = "Number of words to return") int numberOfWords,
            final @ApiParam(value = "The actual word") String theWord,
            final @ApiParam(value = "Any combination of parts of speech") List<String> pos,
            final @ApiParam(value = "The definition") String def,
            final @ApiParam(value = "The origin") String origin,
            final @ApiParam(value = "Example usage of the word") String example,
            final @ApiParam(value = "Any combination of tags") List<String> tags,
            final @ApiParam(value = "The note") String note,
            final @ApiParam(value = "The username of the person who created the word") String creator,
            final @ApiParam(value = "A list of word IDs") List<String> uuids
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

}

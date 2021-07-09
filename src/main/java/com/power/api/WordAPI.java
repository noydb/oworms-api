package com.power.api;

import com.power.dto.WordDTO;
import com.power.error.OWormException;
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
                    message = "Any other internal error while creating word"
            )
    })
    ResponseEntity<WordDTO> create(WordDTO wordDTO, String permissionKey, String user) throws OWormException;

    @ApiOperation(
            value = "Retrieves all words meeting the criteria provided by the query parameters. " +
                    "If no query parameters are specified, all words will be returned." +
                    "If no words exist, an empty array will be returned.",
            response = List.class
    )
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Retrieved all words"
            ),
            @ApiResponse(
                    code = 500,
                    message = "Internal error while retrieving all the words"
            )
    })
    ResponseEntity<List<WordDTO>> retrieveAll(
            String theWord,
            String definition,
            List<String> partsOfSpeech,
            String creator,
            String haveLearnt
    );

    @ApiOperation(
            value = "Retrieves a single word using the specified ID",
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
                    message = "An error occurred while retrieving the word"
            )
    })
    ResponseEntity<WordDTO> retrieve(Long wordId);

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
                    message = "An error occurred while retrieving the random word"
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
                    message = "Any other internal error while updating the word"
            )
    })
    ResponseEntity<WordDTO> update(Long wordId, String permissionKey, WordDTO updatedWord) throws OWormException;

}

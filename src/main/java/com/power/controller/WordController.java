package com.power.controller;

import com.power.controller.api.WordAPI;
import com.power.dto.WordDTO;
import com.power.service.WordService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/o/worms")
public class WordController implements WordAPI {

    private final WordService service;

    public WordController(final WordService service) {
        this.service = service;
    }

    @Override
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WordDTO> create(final @Valid @RequestBody WordDTO wordDTO,
                                          final @RequestParam(value = "permission_key") String permissionKey,
                                          final @RequestParam(value = "u") String user) {
        WordDTO created = service.create(wordDTO, permissionKey, user);

        return ResponseEntity.ok(created);
    }

    @Override
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<WordDTO>> retrieveAll(@RequestParam(value = "w", required = false) String theWord,
                                                     @RequestParam(value = "def", required = false) String definition,
                                                     @RequestParam(value = "pos", required = false) List<String> partsOfSpeech,
                                                     @RequestParam(value = "creator", required = false) String creator,
                                                     @RequestParam(value = "learnt", required = false) String haveLearnt) {
        return ResponseEntity.ok(
                service.retrieveAll(theWord, definition, partsOfSpeech, creator, haveLearnt)
        );
    }

    @Override
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WordDTO> retrieve(@PathVariable("id") Long wordId) {
        return ResponseEntity.ok(service.retrieve(wordId));
    }

    @Override
    @GetMapping(
            value = "/random",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WordDTO> retrieveRandom() {
        return ResponseEntity.ok(service.retrieveRandom());
    }

    @Override
    @GetMapping(
            value = "/oxford/{theWord}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> oxfordRetrieve(@PathVariable("theWord") String theWord,
                                                 @RequestParam(value = "permission_key") String permissionKey) {
        return service.oxfordRetrieve(theWord, permissionKey);
    }

    @Override
    @PutMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WordDTO> update(@PathVariable("id") Long wordId,
                                          @RequestParam(value = "permission_key") String permissionKey,
                                          @RequestBody WordDTO updatedWord) {
        WordDTO updatedWordDTO = service.update(wordId, updatedWord, permissionKey);

        return ResponseEntity.ok().body(updatedWordDTO);
    }
}

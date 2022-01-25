package com.power.controller;

import com.power.controller.api.WordAPI;
import com.power.dto.WordDTO;
import com.power.dto.WordRequestDTO;
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
    public ResponseEntity<WordDTO> create(final @Valid @RequestBody WordRequestDTO wordRequestDTO,
                                          final @RequestParam(value = "permission_key") String permissionKey,
                                          final @RequestParam(value = "u") String user) {
//        LogUtil.log("Creating word", wordRequestDTO);

        WordDTO created = service.create(wordRequestDTO, permissionKey, user);

        return ResponseEntity.ok(created);
    }

    @Override
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<WordDTO>> retrieveAll(@RequestParam(value = "word", required = false) String word,
                                                     @RequestParam(value = "pos", required = false) List<String> pos,
                                                     @RequestParam(value = "def", required = false) String def,
                                                     @RequestParam(value = "ori", required = false) String origin,
                                                     @RequestParam(value = "ex", required = false) String example,
                                                     @RequestParam(value = "tags", required = false) List<String> tags,
                                                     @RequestParam(value = "note", required = false) String note,
                                                     @RequestParam(value = "creator", required = false) String creator) {
//        LogUtil.log("Retrieving all words");

        return ResponseEntity.ok(
                service.retrieveAll(word, pos, def, origin, example, tags, note, creator)
        );
    }

    @Override
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WordDTO> retrieve(@PathVariable("id") Long wordId) {
//        LogUtil.log("Retrieving word with ID: " + wordId);

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
//        LogUtil.log("Retrieving data from oxford API");

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
                                          @RequestBody WordRequestDTO wordRequestDTO) {
//        LogUtil.log("Updating word #" + wordId, wordRequestDTO);

        WordDTO updatedWordDTO = service.update(wordId, wordRequestDTO, permissionKey);

        return ResponseEntity.ok().body(updatedWordDTO);
    }
}

package com.power.oworms.word.controller;

import com.power.oworms.common.util.LogUtil;
import com.power.oworms.word.controller.api.WordAPI;
import com.power.oworms.word.dto.StatisticsDTO;
import com.power.oworms.word.dto.WordDTO;
import com.power.oworms.word.dto.WordRequestDTO;
import com.power.oworms.word.service.WordService;
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
                                          @RequestParam("u") String uname,
                                          @RequestParam("bna") String banana) {
        LogUtil.log("Creating new word");

        WordDTO created = service.create(wordRequestDTO, uname, banana);

        return ResponseEntity.ok(created);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WordDTO>> retrieveAll(@RequestParam(value = "word", required = false) String word,
                                                     @RequestParam(value = "pos", required = false) List<String> pos,
                                                     @RequestParam(value = "def", required = false) String def,
                                                     @RequestParam(value = "ori", required = false) String origin,
                                                     @RequestParam(value = "ex", required = false) String example,
                                                     @RequestParam(value = "tags", required = false) List<String> tags,
                                                     @RequestParam(value = "note", required = false) String note,
                                                     @RequestParam(value = "creator", required = false) String creator) {
        LogUtil.log("Retrieving all words");

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
        LogUtil.log("Retrieving word with ID: " + wordId);

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
                                                 @RequestParam("u") String uname,
                                                 @RequestParam("bna") String banana) {
        LogUtil.log("Calling oxford api");

        return service.oxfordRetrieve(theWord, uname, banana);
    }

    @Override
    @PutMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WordDTO> update(@PathVariable("id") Long wordId,
                                          @RequestBody WordRequestDTO wordRequestDTO,
                                          @RequestParam("u") String uname,
                                          @RequestParam("bna") String banana) {
        LogUtil.log("Updating word #" + wordId);

        WordDTO updatedWordDTO = service.update(wordId, wordRequestDTO, uname, banana);

        return ResponseEntity.ok().body(updatedWordDTO);
    }

    @GetMapping(
            value = "/statistics",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<StatisticsDTO> getStatistics() {
        LogUtil.log("Retrieving statistics");

        return ResponseEntity.ok(service.getStatistics());
    }
}

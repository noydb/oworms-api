package com.oworms.word.controller;

import com.oworms.word.controller.api.WordAPI;
import com.oworms.word.dto.WordDTO;
import com.oworms.word.service.WordService;
import com.oworms.common.util.LogUtil;
import com.oworms.word.dto.StatisticsDTO;
import com.oworms.word.dto.WordRequestDTO;
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
                                          @RequestParam("u") String u,
                                          @RequestParam("bna") String banana) {
        LogUtil.log("Creating new word");

        WordDTO created = service.create(wordRequestDTO, u, banana);

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
            value = "/{uuid}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WordDTO> retrieve(@PathVariable("uuid") String uuid) {
        LogUtil.log("Retrieving word with uuid: " + uuid);

        return ResponseEntity.ok(service.retrieve(uuid));
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
    @PutMapping(
            value = "/{uuid}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WordDTO> update(@PathVariable("uuid") String uuid,
                                          @RequestBody WordRequestDTO wordRequestDTO,
                                          @RequestParam("u") String u,
                                          @RequestParam("bna") String banana) {
        LogUtil.log("Updating word with uuid: " + uuid);

        WordDTO updatedWordDTO = service.update(uuid, wordRequestDTO, u, banana);

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

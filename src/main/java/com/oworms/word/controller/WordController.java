package com.oworms.word.controller;

import com.oworms.util.LogUtil;
import com.oworms.word.controller.api.WordAPI;
import com.oworms.word.dto.WordDTO;
import com.oworms.word.dto.WordFilter;
import com.oworms.word.dto.WordRequestDTO;
import com.oworms.word.service.WordService;
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
    public ResponseEntity<WordDTO> create(final @RequestBody @Valid WordRequestDTO wordRequestDTO,
                                          final @RequestParam("u") String u,
                                          final @RequestParam("bna") String banana) {
        LogUtil.log("Creating new word");

        WordDTO created = service.create(wordRequestDTO, u, banana);

        return ResponseEntity.ok(created);
    }

    @Override
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<WordDTO>> retrieveAll(
            final @RequestParam(value = "numberOfWords") int numberOfWords,
            final @RequestParam(value = "word", required = false) String word,
            final @RequestParam(value = "pos", required = false) List<String> pos,
            final @RequestParam(value = "def", required = false) String def,
            final @RequestParam(value = "ori", required = false) String origin,
            final @RequestParam(value = "ex", required = false) String example,
            final @RequestParam(value = "tags", required = false) List<String> tags,
            final @RequestParam(value = "note", required = false) String note,
            final @RequestParam(value = "creator", required = false) String creator,
            final @RequestParam(value = "uuids", required = false) List<String> uuids) {
        LogUtil.log("Retrieving all words");

        final WordFilter wordFilter = new WordFilter(numberOfWords, word, pos, def, origin, example, tags, note, creator, uuids);

        return ResponseEntity.ok(service.retrieveAll(wordFilter));
    }

    @Override
    @GetMapping(
            value = "/{uuid}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WordDTO> retrieve(final @PathVariable("uuid") String uuid) {
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
    public ResponseEntity<WordDTO> update(final @PathVariable("uuid") String uuid,
                                          final @RequestBody @Valid WordRequestDTO wordRequestDTO,
                                          final @RequestParam("u") String u,
                                          final @RequestParam("bna") String banana) {
        LogUtil.log("Updating word with uuid: " + uuid);

        WordDTO updatedWordDTO = service.update(uuid, wordRequestDTO, u, banana);

        return ResponseEntity.ok().body(updatedWordDTO);
    }

}

package com.oworms.word.controller;

import com.oworms.word.dto.TagDTO;
import com.oworms.word.service.TagService;
import com.oworms.common.util.LogUtil;
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
@RequestMapping(value = "/o/tags")
public class TagController {

    private final TagService service;

    public TagController(final TagService service) {
        this.service = service;
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TagDTO> create(final @Valid @RequestBody TagDTO tagDTO) {
        LogUtil.log("Creating tag", tagDTO);

        TagDTO created = service.create(tagDTO);

        return ResponseEntity.ok(created);
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<TagDTO>> retrieveAll(@RequestParam(value = "name", required = false) String name,
                                                    @RequestParam(value = "word", required = false) String theWord) {
        LogUtil.log("Retrieving all tags");

        return ResponseEntity.ok(service.retrieveAll(name));
    }

    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TagDTO> retrieve(@PathVariable("id") Long tagId) {
        LogUtil.log("Retrieving tag with ID: " + tagId);

        return ResponseEntity.ok(service.retrieve(tagId));
    }

    @PutMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TagDTO> update(@PathVariable("id") Long tagId,
                                         @RequestBody TagDTO updatedTag) {
        LogUtil.log("Updating tag #" + tagId + " name to " + updatedTag.getName());

        TagDTO updatedTagDTO = service.update(tagId, updatedTag);

        return ResponseEntity.ok().body(updatedTagDTO);
    }

}

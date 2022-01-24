package com.power.controller;

import com.power.controller.api.TagAPI;
import com.power.dto.TagDTO;
import com.power.service.TagService;
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
public class TagController implements TagAPI {

    private final TagService service;

    public TagController(final TagService service) {
        this.service = service;
    }

    @Override
    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TagDTO> create(final @Valid @RequestBody TagDTO tagDTO,
                                         final @RequestParam(value = "permission_key") String permissionKey) {
//        LogUtil.log("Creating tag", tagDTO);

        TagDTO created = service.create(tagDTO, permissionKey);

        return ResponseEntity.ok(created);
    }

    @Override
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<TagDTO>> retrieveAll(@RequestParam(value = "name", required = false) String name,
                                                    @RequestParam(value = "word", required = false) String theWord) {
//        LogUtil.log("Retrieving all tags");

        return ResponseEntity.ok(service.retrieveAll(name));
    }

    @Override
    @GetMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TagDTO> retrieve(@PathVariable("id") Long tagId) {
//        LogUtil.log("Retrieving tag with ID: " + tagId);

        return ResponseEntity.ok(service.retrieve(tagId));
    }

    @Override
    @PutMapping(
            value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<TagDTO> update(@PathVariable("id") Long tagId,
                                         @RequestParam(value = "permission_key") String permissionKey,
                                         @RequestBody TagDTO updatedTag) {
//        LogUtil.log("Updating tag " + updatedTag.getName(), updatedTag);

        TagDTO updatedTagDTO = service.update(tagId, updatedTag, permissionKey);

        return ResponseEntity.ok().body(updatedTagDTO);
    }

}

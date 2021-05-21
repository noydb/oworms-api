package com.power.controller;

import com.power.dto.WordDTO;
import com.power.service.WordService;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/o/worms")
public class WordController {

    private final WordService service;

    public WordController(final WordService service) {
        this.service = service;
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> create(@RequestBody WordDTO wordDTO) {
        if (null == wordDTO) {
            return ResponseEntity.badRequest().build();
        }

        service.create(wordDTO);

        Link link = linkTo(methodOn(WordController.class).create(wordDTO))
                .slash(wordDTO.getId())
                .withRel("retrieve");

        return ResponseEntity.created(link.toUri()).build();
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<WordDTO>> retrieveAll() {
        return ResponseEntity.ok(service.retrieveAll());
    }

    @GetMapping(
            value = "/{word}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WordDTO> retrieve(@PathVariable("word") String theWord) {
        return ResponseEntity.ok(service.retrieve(theWord));
    }

    @PostMapping(
            value = "/read"
    )
    public ResponseEntity<Void> readCSV(@RequestParam("excel_file") MultipartFile excelFile) {
        boolean success = service.readCSV(excelFile);

        return success ? ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

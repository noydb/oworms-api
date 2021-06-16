package com.power.controller;

import com.power.dto.WordDTO;
import com.power.service.SecurityService;
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

import javax.validation.Valid;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/o/worms")
public class WordController {

    private final WordService service;
    private final SecurityService ss;

    public WordController(final WordService service,
                          final SecurityService ss) {
        this.service = service;
        this.ss = ss;
    }

    @PostMapping(
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> create(final @Valid @RequestBody WordDTO wordDTO,
                                       final @RequestParam(value = "u", required = false) String u) {
        if (null == wordDTO || null == u) {
            return ResponseEntity.badRequest().build();
        }

        if (ss.unknownUser(u)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        service.create(wordDTO, u);

        Link link = linkTo(methodOn(WordController.class).create(wordDTO, u))
                .slash(wordDTO.getId())
                .withRel("retrieve");

        return ResponseEntity.created(link.toUri()).build();
    }

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<List<WordDTO>> retrieveAll(@RequestParam(value = "w", required = false) String theWord,
                                                     @RequestParam(value = "def", required = false) String definition,
                                                     @RequestParam(value = "pos", required = false) String partOfSpeech,
                                                     @RequestParam(value = "creator", required = false) String creator,
                                                     @RequestParam(value = "learnt", required = false) String haveLearnt) {
        return ResponseEntity.ok(
                service.retrieveAll(theWord, definition, partOfSpeech, creator, haveLearnt)
        );
    }

    @GetMapping(
            value = "/{word}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<WordDTO> retrieve(@PathVariable("word") String theWord) {
        return ResponseEntity.ok(service.retrieve(theWord));
    }

    @PostMapping(
            value = "/read",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> readCSV(@RequestParam("excel_file") MultipartFile excelFile) {
        boolean success = service.readCSV(excelFile);

        return success ? ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}

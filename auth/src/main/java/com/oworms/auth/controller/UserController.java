package com.oworms.auth.controller;

import com.oworms.auth.dto.NewUserDTO;
import com.oworms.auth.dto.UserDTO;
import com.oworms.auth.error.AccountExistsException;
import com.oworms.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping(path = "/o/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping(
            value = "/create", produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Void> create(@Valid @RequestBody NewUserDTO newUserDTO,
                                       @RequestParam(value = "u") String username,
                                       @RequestParam(value = "bna") String banana) throws AccountExistsException {
        userService.create(newUserDTO, username, banana);

        return ResponseEntity.ok().build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> retrieve(@RequestParam(value = "u") String username,
                                            @RequestParam(value = "bna") String banana) {
        return ResponseEntity.ok().body(userService.retrieve(username, banana));
    }

    @PutMapping(
            value = "/{userUUID}", produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDTO> update(@PathVariable String userUUID,
                                          @Valid @RequestBody UserDTO userDTO,
                                          @RequestParam(value = "u") String username,
                                          @RequestParam(value = "bna") String banana) throws AccountExistsException {
        userService.updateUser(userUUID, userDTO, username, banana);

        return ResponseEntity.ok(userDTO);
    }

    @PatchMapping(
            value = "/word/{wordUUID}/like"
    )
    public ResponseEntity<Void> likeWord(@PathVariable String wordUUID,
                                         @RequestParam(value = "u") String username,
                                         @RequestParam(value = "bna") String banana) {
        userService.likeWord(wordUUID, username, banana);

        return ResponseEntity.noContent().build();
    }
}

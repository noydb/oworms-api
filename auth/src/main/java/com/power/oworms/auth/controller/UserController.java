package com.power.oworms.auth.controller;

import com.power.oworms.auth.dto.NewUserDTO;
import com.power.oworms.auth.dto.UserDTO;
import com.power.oworms.auth.error.AccountExistsException;
import com.power.oworms.auth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @PutMapping(
            value = "/{userId}", produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<UserDTO> update(@PathVariable("userId") String userId,
                                          @Valid @RequestBody UserDTO userDTO,
                                          @RequestParam(value = "u") String username,
                                          @RequestParam(value = "bna") String banana) throws AccountExistsException {
        userService.updateUser(userId, userDTO, username, banana);

        return ResponseEntity.ok(userDTO);
    }
}

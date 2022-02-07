package com.power.oworms.auth.controller;

import com.power.oworms.auth.service.SettingsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/o/settings")
public class SettingsController {

    private final SettingsService service;

    public SettingsController(final SettingsService service) {
        this.service = service;
    }

    @GetMapping(value = "weekly", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> weeklyAdmin(@RequestParam("u") String username, @RequestParam("bna") String banana) {
        service.doWeeklyAdmin(username, banana);

        return ResponseEntity.ok().build();
    }
}

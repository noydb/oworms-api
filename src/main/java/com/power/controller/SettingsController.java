package com.power.controller;

import com.power.dto.StatisticsDTO;
import com.power.service.SettingsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/o/worms/stats")
public class SettingsController {

    private final SettingsService service;

    public SettingsController(final SettingsService service) {
        this.service = service;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatisticsDTO> getStatistics(final @RequestParam(value = "u", required = false) String u) {
        return ResponseEntity.ok(service.getStatistics());
    }
}

package com.power.controller;

import com.power.dto.StatisticsDTO;
import com.power.service.SecurityService;
import com.power.service.SettingsService;
import org.springframework.http.HttpStatus;
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
    private final SecurityService ss;

    public SettingsController(final SettingsService service,
                              final SecurityService ss) {
        this.service = service;
        this.ss = ss;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StatisticsDTO> getStatistics(final @RequestParam(value = "u", required = false) String u) {
        if (ss.unknownUser(u)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.ok(service.getStatistics());
    }
}

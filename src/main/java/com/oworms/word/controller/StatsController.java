package com.oworms.word.controller;

import com.oworms.common.util.LogUtil;
import com.oworms.word.controller.api.StatsAPI;
import com.oworms.word.dto.StatisticsDTO;
import com.oworms.word.service.StatisticsService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/o/stats")
public class StatsController implements StatsAPI {

    private final StatisticsService service;

    public StatsController(final StatisticsService service) {
        this.service = service;
    }

    @Override
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<StatisticsDTO> getStats() {
        LogUtil.log("Retrieving statistics");
        return ResponseEntity.ok(service.getStatistics());
    }
}

package com.tus.traunreut.controller;

import com.tus.traunreut.service.LeagueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/league")
public class LeagueController {
    private final LeagueService leagueService;

    public LeagueController(LeagueService leagueService) {
        this.leagueService = leagueService;
    }

    @GetMapping("/name")
    public ResponseEntity<List<String>> getDistinctLeagueNames() {
        return new ResponseEntity<>(leagueService.getDistinctLeagueNames(), HttpStatus.OK);
    }
}

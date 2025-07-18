package com.tus.traunreut.controller;

import com.tus.traunreut.League;
import com.tus.traunreut.service.LeagueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @GetMapping
    public ResponseEntity<List<League>> getLeagues() {
        return new ResponseEntity<>(leagueService.getLeagues(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<League> updateLeague(@PathVariable Long id, @RequestBody League updatedLeague) {
        League league = leagueService.updateLeague(id, updatedLeague);
        return ResponseEntity.ok(league);
    }

    @PostMapping
    public ResponseEntity<League> createLeague(@RequestBody League league) {
        League createdLeague = leagueService.createLeague(league);
        return new ResponseEntity<>(createdLeague, HttpStatus.CREATED);
    }
}

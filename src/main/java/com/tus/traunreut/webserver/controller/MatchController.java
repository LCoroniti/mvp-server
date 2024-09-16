package com.tus.traunreut.webserver.controller;

import com.tus.traunreut.webserver.dto.MatchDTO;
import com.tus.traunreut.webserver.model.Match;
import com.tus.traunreut.webserver.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/matches")
public class MatchController {
    private final MatchService matchService;

    public MatchController(@Autowired MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }

    @GetMapping("/next")
    public MatchDTO getNextMatch() {
        return matchService.getNextMatch();
    }

    @GetMapping("/{id}")
    public Match getMatchById(@PathVariable String id) {
        return matchService.getMatchById(id);
    }

    @PostMapping
    public Match createMatch(@RequestBody Match match) {
        System.out.println("Creating match: " + match.toString());
        return matchService.saveMatch(match);
    }

    @PutMapping("/{id}")
    public Match addPlayersToMatch(@PathVariable String id, @RequestBody List<String> playerIds) {
        return matchService.addPlayersToMatch(id, playerIds);
    }
}

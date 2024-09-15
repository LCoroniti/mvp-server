package com.tus.traunreut.webserver.controller;

import com.tus.traunreut.webserver.model.Team;
import com.tus.traunreut.webserver.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping
    public ResponseEntity<Team> saveTeamForClub(@RequestBody Team team, @RequestParam String clubName) {
        try {
            return ResponseEntity.ok(teamService.saveTeamForClub(team, clubName));
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}

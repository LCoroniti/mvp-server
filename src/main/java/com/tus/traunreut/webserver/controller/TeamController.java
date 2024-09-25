package com.tus.traunreut.webserver.controller;

import com.tus.traunreut.webserver.model.Team;
import com.tus.traunreut.webserver.service.TeamService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping
    public ResponseEntity<List<Team>> getAllTeams() {
        List<Team> teams = teamService.getAllTeams();
        return new ResponseEntity<>(teams, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Team> getTeamById(@PathVariable Long id) {
        Optional<Team> team = teamService.getTeamById(id);
        return team.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Team> createTeam(@RequestBody Team team) {
        Team savedTeam = teamService.saveTeam(team);
        return new ResponseEntity<>(savedTeam, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable Long id, @RequestBody Team team) {
        if (!teamService.getTeamById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        team.setId(id);  // Ensure the id is set for the update
        Team updatedTeam = teamService.saveTeam(team);
        return ResponseEntity.ok(updatedTeam);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTeam(@PathVariable Long id) {
        if (!teamService.getTeamById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        teamService.deleteTeam(id);
        return ResponseEntity.noContent().build();
    }
}

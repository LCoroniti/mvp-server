package com.tus.traunreut.webserver.controller;

import com.tus.traunreut.webserver.model.Player;
import com.tus.traunreut.webserver.service.VotingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/vote")
public class VoteController {
    private final VotingService votingService;

    public VoteController(VotingService votingService) {
        this.votingService = votingService;
    }

    @PostMapping
    public ResponseEntity<String> vote(@RequestParam Long playerId, @RequestParam Long matchId, @RequestParam String voterToken) {
        if (votingService.vote(playerId, matchId, voterToken)) {
            return ResponseEntity.ok("Vote accepted");
        } else {
            return ResponseEntity.badRequest().body("Vote already cast");
        }
    }

    @GetMapping("/check")
    public ResponseEntity<Long> hasUserVoted(@RequestParam Long matchId, @RequestParam String voterToken) {
        Optional<Player> player = votingService.getUserVote(matchId, voterToken);
        return player.map(value -> ResponseEntity.ok(value.getId())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteVote(@RequestParam Long matchId, @RequestParam String voterToken) {
        votingService.deleteVote(matchId, voterToken);
        return ResponseEntity.ok().build();
    }
}

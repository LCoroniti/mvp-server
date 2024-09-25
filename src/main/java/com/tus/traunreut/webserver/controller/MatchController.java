package com.tus.traunreut.webserver.controller;

import com.tus.traunreut.webserver.dto.MatchDto;
import com.tus.traunreut.webserver.model.Match;
import com.tus.traunreut.webserver.service.external.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.tus.traunreut.webserver.service.MatchService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/match")
public class MatchController {
    private final MatchService matchService;
    private final ImageService imageService;

    public MatchController(MatchService matchService, ImageService imageService) {
        this.matchService = matchService;
        this.imageService = imageService;
    }

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches() {
        List<Match> matches = matchService.getAllMatches();
        return new ResponseEntity<>(matches, HttpStatus.OK);
    }

    @GetMapping("/weekend")
    public ResponseEntity<List<MatchDto>> getAllMatchesThisWeekend() {
        List<Match> matches = matchService.getAllMatchesThisWeekend();

        if (matches.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        List<CompletableFuture<MatchDto>> futureMatchDtos = matches.stream()
                .map(match -> CompletableFuture.supplyAsync(() -> {
                    String homeTeamLogo = null;
                    String guestTeamLogo = null;

                    try {
                        homeTeamLogo = imageService.fetchLogoBase64(match.getHomeTeam().getClub().getLogoUrl());
                    } catch (Exception e) {
                        // Log the error and continue, you can return a placeholder or null logo if needed
                        System.err.println("Failed to fetch home team logo for match: " + match.getId());
                        e.printStackTrace();
                    }

                    try {
                        guestTeamLogo = imageService.fetchLogoBase64(match.getGuestTeam().getClub().getLogoUrl());
                    } catch (Exception e) {
                        // Log the error and continue, you can return a placeholder or null logo if needed
                        System.err.println("Failed to fetch guest team logo for match: " + match.getId());
                        e.printStackTrace();
                    }

                    // Return the MatchDto with the fetched logos
                    return new MatchDto(match, homeTeamLogo, guestTeamLogo);
                }))
                .toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureMatchDtos.toArray(new CompletableFuture[0]));

        // Wait for all futures to complete
        List<MatchDto> matchDtos = allOf.thenApply(v -> futureMatchDtos.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()))
                .join();
        return new ResponseEntity<>(matchDtos, HttpStatus.OK);
    }

    @GetMapping("/next")
    public ResponseEntity<MatchDto> getNextMatch() {
        Optional<Match> nextMatch = matchService.getNextMatch();
        if (nextMatch.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Match match = nextMatch.get();
        String homeTeamLogo;
        String guestTeamLogo;
        try {
            homeTeamLogo = imageService.fetchLogoBase64(match.getHomeTeam().getClub().getLogoUrl());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        try {
            guestTeamLogo = imageService.fetchLogoBase64(match.getGuestTeam().getClub().getLogoUrl());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return new ResponseEntity<>(new MatchDto(match, homeTeamLogo, guestTeamLogo), HttpStatus.OK);
    }
}

package com.tus.traunreut.controller;

import com.tus.traunreut.Match;
import com.tus.traunreut.dto.history.HistoryMatchDto;
import com.tus.traunreut.dto.voting.VotingMatchDto;
import com.tus.traunreut.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {
    private final MatchService matchService;

    @GetMapping
    public ResponseEntity<List<Match>> getAllMatches(@RequestParam(required = false) Long leagueId) {
        if (leagueId == null) {
            return new ResponseEntity<>(matchService.getAllMatches(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(matchService.getAllMatchesFromLeague(leagueId), HttpStatus.OK);
        }
    }

    @GetMapping("/past")
    public ResponseEntity<PagedModel<EntityModel<HistoryMatchDto>>> getAllVotedMatches(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) String leagueName,
            PagedResourcesAssembler<HistoryMatchDto> pagedAssembler) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HistoryMatchDto> pastMatches = matchService.getAllPastMatchesWithVotes(pageable, leagueName);

        List<CompletableFuture<Void>> futures = pastMatches.getContent().stream().map(matchDto -> {
            CompletableFuture<String> homeLogoFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return matchDto.getMatch().getHomeTeam().getClub().getLogoBase64();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
            CompletableFuture<String> guestLogoFuture = CompletableFuture.supplyAsync(() -> {
                try {
                    return matchDto.getMatch().getGuestTeam().getClub().getLogoBase64();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            return CompletableFuture.allOf(homeLogoFuture, guestLogoFuture).thenAccept(ignored -> {
                try {
                    String homeLogo = homeLogoFuture.get();
                    String guestLogo = guestLogoFuture.get();
                    matchDto.setHomeTeamLogoBase64(homeLogo);
                    matchDto.setGuestTeamLogoBase64(guestLogo);
                } catch (Exception e) {
                    System.err.println("Error fetching logos: " + e.getMessage());
                }
            });
        }).toList();

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        PagedModel<EntityModel<HistoryMatchDto>> model = pagedAssembler.toModel(pastMatches);
        return new ResponseEntity<>(model, HttpStatus.OK);
    }

    @GetMapping("/week")
    public ResponseEntity<List<VotingMatchDto>> getAllMatchesCurrentWeek() {
        List<Match> matches = matchService.getAllMatchesCurrentWeek();

        if (matches.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }

        List<CompletableFuture<VotingMatchDto>> futureMatchDtos = matches.stream()
                .map(match -> CompletableFuture.supplyAsync(() -> {
                    String homeTeamLogo = null;
                    String guestTeamLogo = null;

                    try {
                        homeTeamLogo = match.getHomeTeam().getClub().getLogoBase64();
                    } catch (Exception e) {
                        System.err.println("Failed to fetch home team logo for match: " + match.getId());
                    }

                    try {
                        guestTeamLogo = match.getGuestTeam().getClub().getLogoBase64();
                    } catch (Exception e) {
                        System.err.println("Failed to fetch guest team logo for match: " + match.getId());
                    }

                    // Return the MatchDto with the fetched logos
                    return new VotingMatchDto(match, homeTeamLogo, guestTeamLogo);
                }))
                .toList();

        CompletableFuture<Void> allOf = CompletableFuture.allOf(futureMatchDtos.toArray(new CompletableFuture[0]));

        // Wait for all futures to complete
        List<VotingMatchDto> matchDtos = allOf.thenApply(v -> futureMatchDtos.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toList()))
                .join();
        return new ResponseEntity<>(matchDtos, HttpStatus.OK);
    }

    @GetMapping("/next")
    public ResponseEntity<VotingMatchDto> getNextMatch() {
        Optional<Match> nextMatch = matchService.getNextMatch();
        if (nextMatch.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        Match match = nextMatch.get();
        String homeTeamLogo;
        String guestTeamLogo;
        try {
            homeTeamLogo = match.getHomeTeam().getClub().getLogoBase64();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        try {
            guestTeamLogo = match.getGuestTeam().getClub().getLogoBase64();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return new ResponseEntity<>(new VotingMatchDto(match, homeTeamLogo, guestTeamLogo), HttpStatus.OK);
    }

    @GetMapping("/{matchId}")
    public ResponseEntity<VotingMatchDto> getMatch(@PathVariable Long matchId) {
        Optional<Match> idMatch = matchService.getMatchById(matchId);
        if (idMatch.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        Match match = idMatch.get();
        String homeTeamLogo;
        String guestTeamLogo;
        try {
            homeTeamLogo = match.getHomeTeam().getClub().getLogoBase64();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        try {
            guestTeamLogo = match.getGuestTeam().getClub().getLogoBase64();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
        return new ResponseEntity<>(new VotingMatchDto(match, homeTeamLogo, guestTeamLogo), HttpStatus.OK);
    }

    @PutMapping("/{matchId}")
    public ResponseEntity<Match> updateMatch(@PathVariable Long matchId, @RequestBody Match matchDetails) {
        Match updatedMatch = matchService.updateMatch(matchId, matchDetails);
        return ResponseEntity.ok(updatedMatch);
    }
}

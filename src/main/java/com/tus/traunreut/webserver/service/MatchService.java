package com.tus.traunreut.webserver.service;

import com.tus.traunreut.webserver.dto.PlayerDto;
import com.tus.traunreut.webserver.dto.history.HistoryMatchDto;
import com.tus.traunreut.webserver.dto.history.VoteDto;
import com.tus.traunreut.webserver.model.*;
import com.tus.traunreut.webserver.repository.MatchPlayerRepository;
import com.tus.traunreut.webserver.repository.MatchRepository;
import com.tus.traunreut.webserver.repository.VoteRepository;
import com.tus.traunreut.webserver.service.schedule.TaskScheduler;
import com.tus.traunreut.webserver.service.scraper.ScraperService;
import com.tus.traunreut.webserver.util.DateTimeUtil;
import jakarta.transaction.Transactional;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final VoteRepository voteRepository;
    private final MatchPlayerRepository matchPlayerRepository;
    private final ScraperService scraperService;

    public MatchService(MatchRepository matchRepository, VoteRepository voteRepository, MatchPlayerRepository matchPlayerRepository, ScraperService scraperService) {
        this.matchRepository = matchRepository;
        this.voteRepository = voteRepository;
        this.matchPlayerRepository = matchPlayerRepository;
        this.scraperService = scraperService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onStartup() {
        try {
            List<Match> scrapedMatches = scraperService.scrapeMatches();
            removeCanceledMatchers(scrapedMatches);
            for (Match match : scrapedMatches) {
                Optional<Match> persistentMatch = getMatch(match.getHomeTeam(), match.getGuestTeam());
                if (persistentMatch.isEmpty()) {
                    matchRepository.save(match);
                } else if (match.hasReport() && !persistentMatch.get().hasReport()) {
                    match.setId(persistentMatch.get().getId());
                    matchRepository.save(match);
                }
                if (match.getNuligaMatchId() != null && !match.hasReport()) {
                    // TODO: Fetch report from handball.net (When statistics for players is integrated)
                } else if (persistentMatch.isPresent()){
                    Match m = persistentMatch.get();
                    if (match.getMatchDate().isAfter(LocalDateTime.now())) {
                        // Schedule the fetching of the MatchPlayers when the Match starts
                        TaskScheduler.getInstance().scheduleTask(() -> {
                            scraperService.scrapeMatchId(m.getHomeTeam().getLeague(), m);
                            List<MatchPlayer> matchPlayers = scraperService.getMatchPlayers(m);
                            matchPlayerRepository.saveAll(matchPlayers);
                            matchRepository.save(m);
                        }, match.getMatchDate());
                    }
                    // Schedule the fetching of the Match report 2 hours after the match started
                    TaskScheduler.getInstance().scheduleTask(() -> {
                        try {
                            Match updated = scraperService.scrapeMatch(m);
                            matchRepository.save(updated);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }, match.getMatchDate().plusHours(2));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Remove matches that are in the db but are not present in the scraped matches.
     */
    public void removeCanceledMatchers(List<Match> scraped) {
        List<Match> persistentMatches = matchRepository.findAll();
        List<Match> removedMatches = persistentMatches.stream().filter(match -> {
            for (Match scrape : scraped) {
                if (scrape.getHomeTeam().equals(match.getHomeTeam()) && scrape.getGuestTeam().equals(match.getGuestTeam())) {
                    return false;
                }
            }
            return true;
        }).toList();
        List<Match> postponedGames = persistentMatches.stream().filter(match -> {
            for (Match scrape : scraped) {
                if (scrape.getHomeTeam().equals(match.getHomeTeam()) && scrape.getGuestTeam().equals(match.getGuestTeam())
                        && !scrape.getMatchDate().equals(match.getMatchDate())) {
                    match.setMatchDate(scrape.getMatchDate());
                    return true;
                }
            }
            return false;
        }).toList();
        matchRepository.deleteAllById(removedMatches.stream().map(Match::getId).toList());
        matchRepository.saveAll(postponedGames);
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public Optional<Match> getNextMatch() {
        return matchRepository.findTopByMatchDateAfterOrderByMatchDateAsc(DateTimeUtil.nowGerman());
    }

    public Optional<Match> getMatchById(Long id) {
        return matchRepository.findById(id);
    }

    @Transactional
    public List<Match> getAllMatchesCurrentWeek() {
        LocalDateTime today = DateTimeUtil.nowGerman().minusHours(1);
        LocalDateTime currentSunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                .toLocalDate()
                .atTime(LocalTime.MAX);
        return matchRepository.findMatchesInTimeRange(today, currentSunday);
    }

    public Page<HistoryMatchDto> getAllPastMatchesWithVotes(Pageable pageable, String leagueName) {
        LocalDateTime now = DateTimeUtil.nowGerman().minusHours(1).minusMinutes(30);

        // Fetch paginated past matches
        Page<Match> pastMatchesPage = null;
        if (leagueName == null || leagueName.isBlank()) {
            pastMatchesPage = matchRepository.findByMatchDateBeforeOrderByMatchDateDesc(now, pageable);
        } else {
            pastMatchesPage = matchRepository.findByMatchDateBeforeAndHomeTeam_League_NameOrderByMatchDateDesc(now, leagueName, pageable);
        }

        List<Match> pastMatches = pastMatchesPage.getContent();

        List<Vote> pastVotes = voteRepository.findByMatchIdIn(
                pastMatches.stream().map(Match::getId).collect(Collectors.toList()));

        Map<Match, List<Vote>> votesByMatchId = pastVotes.stream()
                .collect(Collectors.groupingBy(Vote::getMatch));

        List<MatchPlayerId> pastMatchPlayerIds = votesByMatchId.entrySet().stream()
                .flatMap(entry -> {
                    Match match = entry.getKey();
                    List<Vote> votes = entry.getValue();
                    return votes.stream()
                            .map(vote -> new MatchPlayerId(match.getId(), vote.getPlayer().getId()));
                })
                .toList();

        List<MatchPlayer> pastMatchPlayers = matchPlayerRepository.findByIdIn(pastMatchPlayerIds);

        List<HistoryMatchDto> historyMatchDtos = pastMatches.stream()
                .map(match -> {
                    List<Vote> matchVotes = votesByMatchId.getOrDefault(match, List.of());

                    List<VoteDto> voteDtos = matchVotes.stream()
                            .map(vote -> {
                                MatchPlayer matchPlayer = pastMatchPlayers.stream()
                                        .filter(mp -> mp.getMatch().getId().equals(match.getId()) &&
                                                mp.getPlayer().getId().equals(vote.getPlayer().getId()))
                                        .findFirst()
                                        .orElse(null);

                                PlayerDto playerDto = matchPlayer != null ? new PlayerDto(matchPlayer) : null;
                                return new VoteDto(playerDto);
                            })
                            .collect(Collectors.toList());

                    return new HistoryMatchDto(match, voteDtos);
                })
                .toList();

        calculateVotingResults(historyMatchDtos);
        return new PageImpl<>(historyMatchDtos, pageable, pastMatchesPage.getTotalElements());
    }

    private void calculateVotingResults(List<HistoryMatchDto> matches) {
        matches.forEach(match -> {
            int totalVotes = match.getVotes().size();
            if (totalVotes > 0) {
                Map<String, Integer> playerVoteCounts = new HashMap<>();

                match.getVotes().forEach(vote -> {
                    String playerId = vote.getPlayer().getId();
                    playerVoteCounts.put(playerId, playerVoteCounts.getOrDefault(playerId, 0) + 1);
                });
                playerVoteCounts.forEach((playerId, count) -> {
                    double votingPercentage = (count / (double) totalVotes) * 100;
                    match.getVotes().stream().filter(v -> v.getPlayer().getId().equals(playerId)).findFirst().ifPresent(v -> v.setVotingPercentage(votingPercentage));
                });
            }
        });
    }

    public Optional<Match> getMatch(Team homeTeam, Team guestTeam) {
        return matchRepository.findByHomeTeamAndGuestTeam(homeTeam, guestTeam);
    }
}

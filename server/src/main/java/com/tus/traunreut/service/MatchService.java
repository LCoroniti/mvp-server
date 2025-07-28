package com.tus.traunreut.service;

import com.tus.traunreut.*;
import com.tus.traunreut.dto.PlayerDto;
import com.tus.traunreut.dto.history.HistoryMatchDto;
import com.tus.traunreut.dto.history.VoteDto;
import com.tus.traunreut.repository.MatchPlayerRepository;
import com.tus.traunreut.repository.MatchRepository;
import com.tus.traunreut.repository.VoteRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.javers.core.Javers;
import org.javers.core.diff.Diff;
import org.javers.core.diff.changetype.ValueChange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.stream.Collectors;

import static com.tus.traunreut.Log.DATABASE;
import static com.tus.traunreut.Log.DB_LOG;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final VoteRepository voteRepository;
    private final MatchPlayerRepository matchPlayerRepository;

    @Autowired
    private Javers javers;

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
            DB_LOG.info(DATABASE, "Match will be removed: {} : {} (League = {})", match.getHomeTeam().getName(), match.getGuestTeam().getName(), match.getHomeTeam().getLeague().getName());
            return true;
        }).toList();
        List<Match> postponedGames = persistentMatches.stream().filter(match -> {
            for (Match scrape : scraped) {
                if (scrape.getHomeTeam().equals(match.getHomeTeam()) && scrape.getGuestTeam().equals(match.getGuestTeam())
                        && !scrape.getMatchDate().equals(match.getMatchDate())) {
                    DB_LOG.info(DATABASE, "Match was postponed: {} : {} (League = {}) from {} to {}",
                            match.getHomeTeam().getName(),
                            match.getGuestTeam().getName(),
                            match.getHomeTeam().getLeague().getName(),
                            DateTimeUtil.formatDate(match.getMatchDate()),
                            DateTimeUtil.formatDate(scrape.getMatchDate()));
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

    public List<Match> getAllMatchesFromLeague(Long leagueId) {
        return matchRepository.findByLeagueId(leagueId);
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

    @Transactional
    public Match updateMatch(Long id, Match updatedMatchDetails) {
        Match existingMatch = matchRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Match not found with id: " + id));

        Diff diff = javers.compare(existingMatch, updatedMatchDetails);

        if (diff.hasChanges()) {
            DB_LOG.info(DATABASE, "Updating Match ID {}. Changes: {}", id, diff.getChangesByType(ValueChange.class));

            existingMatch.setMatchDate(updatedMatchDetails.getMatchDate());
            existingMatch.setHomeTeam(updatedMatchDetails.getHomeTeam());
            existingMatch.setGuestTeam(updatedMatchDetails.getGuestTeam());
            existingMatch.setHomeGoals(updatedMatchDetails.getHomeGoals());
            existingMatch.setGuestGoals(updatedMatchDetails.getGuestGoals());
            existingMatch.setNuligaMatchId(updatedMatchDetails.getNuligaMatchId());

            return matchRepository.save(existingMatch);
        } else {
            return existingMatch;
        }
    }
}

package com.tus.traunreut.service.schedule.executors.impl;

import com.tus.traunreut.AbstractIntegrationTest;
import com.tus.traunreut.League;
import com.tus.traunreut.Match;
import com.tus.traunreut.Team;
import com.tus.traunreut.repository.LeagueRepository;
import com.tus.traunreut.repository.MatchRepository;
import com.tus.traunreut.repository.TeamRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class UpdateAllMatchesTaskExecutorTest extends AbstractIntegrationTest {
    @Autowired
    private MatchRepository matchRepository;

    @Autowired
    private LeagueRepository leagueRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private UpdateAllMatchesTaskExecutor taskExecutor;

    private League testLeague;
    private Team homeTeam;
    private Team guestTeam;

    @BeforeEach
    void setUp() {
        // Clean DB
        matchRepository.deleteAll();
        leagueRepository.deleteAll();

        // Create league & teams
        testLeague = new League();
        testLeague.setName("Test League");
        leagueRepository.save(testLeague);

        homeTeam = new Team();
        homeTeam.setName("Home");
        homeTeam.setLeague(testLeague);
        guestTeam = new Team();
        guestTeam.setLeague(testLeague);
        guestTeam.setName("Guest");
        teamRepository.save(homeTeam);
        teamRepository.save(guestTeam);
    }

    @Test
    @Transactional
    void testSyncMatchesFromScraper_updatesExistingMatch() {
        // Given: existing match in DB
        Match dbMatch = new Match();
        dbMatch.setHomeTeam(homeTeam);
        dbMatch.setGuestTeam(guestTeam);
        dbMatch.setMatchDate(LocalDateTime.of(2024, 7, 1, 15, 0));
        dbMatch.setHomeGoals(1);
        dbMatch.setGuestGoals(1);
        dbMatch.setHasReport(false);
        matchRepository.save(dbMatch);

        // When: scraper returns updated data
        Match scrapedMatch = new Match();
        scrapedMatch.setHomeTeam(homeTeam);
        scrapedMatch.setGuestTeam(guestTeam);
        scrapedMatch.setMatchDate(LocalDateTime.of(2024, 7, 2, 15, 0)); // changed date
        scrapedMatch.setHomeGoals(2); // changed home goals
        scrapedMatch.setGuestGoals(1);
        scrapedMatch.setHasReport(true); // changed report

        taskExecutor.syncMatchesFromScraper(List.of(scrapedMatch), testLeague.getId());

        // Then: DB match should be updated
        List<Match> matches = matchRepository.findByLeagueId(testLeague.getId());
        assertEquals(1, matches.size());
        Match updated = matches.get(0);
        assertEquals(scrapedMatch.getMatchDate(), updated.getMatchDate());
        assertEquals(scrapedMatch.getHomeGoals(), updated.getHomeGoals());
        assertTrue(updated.isHasReport());
    }

    @Test
    @Transactional
    void testSyncMatchesFromScraper_deletesMissingMatch() {
        // Given: existing match in DB
        Match dbMatch = new Match();
        dbMatch.setHomeTeam(homeTeam);
        dbMatch.setGuestTeam(guestTeam);
        dbMatch.setMatchDate(LocalDateTime.now());
        matchRepository.save(dbMatch);

        taskExecutor.syncMatchesFromScraper(List.of(), testLeague.getId());

        List<Match> matches = matchRepository.findByLeagueId(testLeague.getId());
        assertEquals(0, matches.size());
    }

    @Test
    @Transactional
    void testSyncMatchesFromScraper_insertsNewMatch() {
        // Given: DB is empty

        // When: scraper returns new match
        Match scrapedMatch = new Match();
        scrapedMatch.setHomeTeam(homeTeam);
        scrapedMatch.setGuestTeam(guestTeam);
        scrapedMatch.setMatchDate(LocalDateTime.now());
        scrapedMatch.setHomeGoals(0);
        scrapedMatch.setGuestGoals(0);
        scrapedMatch.setHasReport(false);

        taskExecutor.syncMatchesFromScraper(List.of(scrapedMatch), testLeague.getId());

        // Then: match should be inserted
        List<Match> matches = matchRepository.findByLeagueId(testLeague.getId());
        assertEquals(1, matches.size());
        Match inserted = matches.get(0);
        assertEquals(scrapedMatch.getMatchDate(), inserted.getMatchDate());
    }
}
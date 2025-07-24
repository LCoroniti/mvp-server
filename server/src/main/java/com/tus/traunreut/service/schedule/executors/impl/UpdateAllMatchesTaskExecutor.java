package com.tus.traunreut.service.schedule.executors.impl;

import com.tus.traunreut.*;
import com.tus.traunreut.repository.LeagueRepository;
import com.tus.traunreut.repository.MatchRepository;
import com.tus.traunreut.scraper.MatchIDScraper;
import com.tus.traunreut.service.MatchParsingService;
import com.tus.traunreut.service.schedule.executors.ScheduledTaskExecutor;
import com.tus.traunreut.service.schedule.tasks.UpdateAllMatchesTask;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.javers.core.Javers;
import org.javers.core.diff.Diff;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.tus.traunreut.Markers.DB_LOG;

@RequiredArgsConstructor
public class UpdateAllMatchesTaskExecutor implements ScheduledTaskExecutor<UpdateAllMatchesTask> {
    private static final Logger databaseLogger = LoggerFactory.getLogger("DATABASE");
    private final MatchRepository matchRepository;
    private final LeagueRepository leagueRepository;
    private final MatchParsingService matchParsingService;
    private final ScraperFactory scraperFactory;
    private final Javers javers;

    @Override
    @Transactional
    public void execute(UpdateAllMatchesTask task) {
        // to update all matches in the database
        // Get all leagues, fetch data of all matches (+ MeetingID), update

        try {
            //for each league:
            List<String> leagues = leagueRepository.findDistinctLeagueNames();
            if (leagues == null || leagues.isEmpty()) {
                databaseLogger.warn("No leagues found in the database. Cannot update matches.");
                return;
            }
            for (String leagueName : leagues) {
                // Step 1: Fetch raw table rows using MatchScraper
                Optional<League> optLeague = leagueRepository.findByName(leagueName);
                if (optLeague.isEmpty())
                {
                    continue;
                }
                League league = optLeague.get();
                IScraper<List<Element>> matchScraper = scraperFactory.createMatchScraper(league);
                List<Element> rows = matchScraper.fetchData();
                //rows is the table -> needs to be parse see MatchScraper commented code
                // Step 2: Parse the rows into Match entities
                List<Match> matches = matchParsingService.parseTableData(rows, league);
                if (matches != null && !matches.isEmpty()) {
                    syncMatchesFromScraper(matches);
                }
                else {
                    databaseLogger.warn("No matches found for league: {}", leagueName);
                }

            }
            databaseLogger.info("UpdateAllMatchesTask executed successfully.");
        } catch (Exception e) {
            databaseLogger.error("Error occurred while executing UpdateAllMatchesTask: ", e);
        }
    }

    private void syncMatchesFromScraper(List<Match> scrapedMatches)
    {
        // Step 1: Build map from scraped matches: key = homeTeamId + guestTeamId
        Map<String, Match> scrapedMap = scrapedMatches.stream()
                .collect(Collectors.toMap(
                        this::buildKey,
                        Function.identity()
                ));

        // Step 2: Fetch all matches currently in DB
        List<Match> dbMatches = matchRepository.findAll();

        for (Match dbMatch : dbMatches) {
            String key = buildKey(dbMatch);

            if (scrapedMap.containsKey(key)) {
                Match scraped = scrapedMap.get(key);

                // Compare with JaVers
                Diff diff = javers.compare(dbMatch, scraped);

                if (diff.hasChanges()) {
                    DB_LOG.info(Markers.DATABASE, "Detected changes for match (home={} guest={}):\n{}",
                            dbMatch.getHomeTeam().getName(),
                            dbMatch.getGuestTeam().getName(),
                            diff.prettyPrint());

                    boolean updated = false;

                    // Update only changed fields
                    if (!Objects.equals(dbMatch.getMatchDate(), scraped.getMatchDate())) {
                        dbMatch.setMatchDate(scraped.getMatchDate());
                        updated = true;
                    }
                    if (!Objects.equals(dbMatch.getHomeGoals(), scraped.getHomeGoals())) {
                        dbMatch.setHomeGoals(scraped.getHomeGoals());
                        updated = true;
                    }
                    if (!Objects.equals(dbMatch.getGuestGoals(), scraped.getGuestGoals())) {
                        dbMatch.setGuestGoals(scraped.getGuestGoals());
                        updated = true;
                    }
                    if (dbMatch.isHasReport() != scraped.isHasReport()) {
                        dbMatch.setHasReport(scraped.isHasReport());
                        updated = true;
                    }

                    if (updated) {
                        matchRepository.save(dbMatch);
                        DB_LOG.info(Markers.DATABASE, "Updated match in DB: id={}", dbMatch.getId());
                    }
                }

                scrapedMap.remove(key); // remove handled match
            } else {
                // Match no longer in scraper → delete
                matchRepository.delete(dbMatch);
                DB_LOG.info(Markers.DATABASE, "Deleted match from DB: home={} guest={}",
                        dbMatch.getHomeTeam().getName(),
                        dbMatch.getGuestTeam().getName());
            }
        }

        // Step 4: Insert new matches that weren't in DB
        for (Match newMatch : scrapedMap.values()) {
            matchRepository.save(newMatch);
        }
    }

    // Helper method to build the key
    private String buildKey(Match match) {
        Long homeId = match.getHomeTeam() != null ? match.getHomeTeam().getId() : null;
        Long guestId = match.getGuestTeam() != null ? match.getGuestTeam().getId() : null;
        return homeId + "_" + guestId;
    }
}
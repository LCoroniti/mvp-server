package com.tus.traunreut.service.schedule.executors.impl;

import com.tus.traunreut.*;
import com.tus.traunreut.events.MatchUpdateEvent;
import com.tus.traunreut.repository.LeagueRepository;
import com.tus.traunreut.repository.MatchRepository;
import com.tus.traunreut.scraper.DataFetchException;
import com.tus.traunreut.scraper.ScraperFactory;
import com.tus.traunreut.service.MatchParsingService;
import com.tus.traunreut.service.schedule.executors.ScheduledTaskExecutor;
import com.tus.traunreut.service.schedule.tasks.UpdateAllMatchesTask;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Element;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.tus.traunreut.Log.DATABASE;
import static com.tus.traunreut.Log.DB_LOG;


@Service
@RequiredArgsConstructor
public class UpdateAllMatchesTaskExecutor implements ScheduledTaskExecutor<UpdateAllMatchesTask> {
    private final MatchRepository matchRepository;
    private final LeagueRepository leagueRepository;
    private final MatchParsingService matchParsingService;
    private final ApplicationEventPublisher eventPublisher;
    private final ScraperFactory scraperFactory;

    @Override
    @Transactional
    public void execute(UpdateAllMatchesTask task) {
        // to update all matches in the database
        // Get all leagues, fetch data of all matches (+ MeetingID), update

        try {
            //for each league:
            List<String> leagues = leagueRepository.findDistinctLeagueNames();
            if (leagues == null || leagues.isEmpty()) {
                DB_LOG.warn(DATABASE, "No leagues found in the database. Cannot update matches.");
                return;
            }
            for (String leagueName : leagues) {
                // Step 1: Fetch raw table rows using MatchScraper
                Optional<League> optLeague = leagueRepository.findByName(leagueName);
                if (optLeague.isEmpty()) {
                    continue;
                }
                League league = optLeague.get();
                IScraper<List<Element>> matchScraper = scraperFactory.createMatchScraper(league);
                List<Element> rows = matchScraper.fetchData();
                //rows is the table -> needs to be parse see MatchScraper commented code
                // Step 2: Parse the rows into Match entities
                List<Match> matches = matchParsingService.parseTableData(rows, league);
                if (matches != null && !matches.isEmpty()) {
                    syncMatchesFromScraper(matches, league.getId());
                } else {
                    DB_LOG.warn(DATABASE, "No matches found for league: {}", leagueName);
                }

            }
            DB_LOG.info(DATABASE, "UpdateAllMatchesTask executed successfully.");
        } catch (DataFetchException e) {
            DB_LOG.error(DATABASE, "Error occurred while executing UpdateAllMatchesTask: ", e);
        }
    }

    void syncMatchesFromScraper(List<Match> scrapedMatches, Long leagueId) {
        // Step 1: Build map from scraped matches: key = homeTeamId + guestTeamId
        Map<String, Match> scrapedMap = scrapedMatches.stream()
                .collect(Collectors.toMap(
                        this::buildKey,
                        Function.identity()
                ));

        // Step 2: Fetch all matches currently in DB
        List<Match> dbMatches = matchRepository.findByLeagueId(leagueId);

        for (Match dbMatch : dbMatches) {
            String key = buildKey(dbMatch);

            if (scrapedMap.containsKey(key)) {
                Match scraped = scrapedMap.get(key);

                boolean updated = false;

                // Update only changed fields
                if (!Objects.equals(dbMatch.getMatchDate(), scraped.getMatchDate())) {
                    DB_LOG.info(Log.DATABASE, "Match date for match {} changed from {} to {}", dbMatch.getId(), DateTimeUtil.formatDate(dbMatch.getMatchDate()), DateTimeUtil.formatDate(scraped.getMatchDate()));
                    dbMatch.setMatchDate(scraped.getMatchDate());
                    updated = true;
                }
                if (!Objects.equals(dbMatch.getHomeGoals(), scraped.getHomeGoals())) {
                    DB_LOG.info(Log.DATABASE, "Home goals for match {} changed from {} to {}", dbMatch.getId(),
                            Objects.requireNonNullElse(dbMatch.getHomeGoals(), 0), Objects.requireNonNullElse(dbMatch.getGuestGoals(), 0));
                    dbMatch.setHomeGoals(scraped.getHomeGoals());
                    updated = true;
                }
                if (!Objects.equals(dbMatch.getGuestGoals(), scraped.getGuestGoals())) {
                    DB_LOG.info(Log.DATABASE, "Guest goals for match {} changed from {} to {}", dbMatch.getId(),
                            Objects.requireNonNullElse(scraped.getHomeGoals(), 0), Objects.requireNonNullElse(scraped.getGuestGoals(), 0));
                    dbMatch.setGuestGoals(scraped.getGuestGoals());
                    updated = true;
                }
                if (dbMatch.isHasReport() != scraped.isHasReport()) {
                    DB_LOG.info(Log.DATABASE, "HasReport changed for match {} from {} to {}", dbMatch.getId(), dbMatch.isHasReport(), scraped.isHasReport());
                    dbMatch.setHasReport(scraped.isHasReport());
                    updated = true;
                }

                if (updated) {
                    matchRepository.save(dbMatch);
                    DB_LOG.info(Log.DATABASE, "Updated match in DB: id={}", dbMatch.getId());
                    eventPublisher.publishEvent(new MatchUpdateEvent(this, dbMatch));
                }

                scrapedMap.remove(key);
            } else {
                // Match no longer in scraper → delete
                matchRepository.delete(dbMatch);
                DB_LOG.info(Log.DATABASE, "Deleted match from DB: home={} guest={}",
                        dbMatch.getHomeTeam().getName(),
                        dbMatch.getGuestTeam().getName());
            }
        }

        // Step 4: Insert new matches that weren't in DB
        for (Match newMatch : scrapedMap.values()) {
            matchRepository.save(newMatch);
            eventPublisher.publishEvent(new MatchUpdateEvent(this, newMatch));
        }
    }

    // Helper method to build the key
    private String buildKey(Match match) {
        Long homeId = match.getHomeTeam() != null ? match.getHomeTeam().getId() : null;
        Long guestId = match.getGuestTeam() != null ? match.getGuestTeam().getId() : null;
        return homeId + "_" + guestId;
    }
}
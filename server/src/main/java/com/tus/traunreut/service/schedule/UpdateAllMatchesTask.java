package com.tus.traunreut.service.schedule;

import com.tus.traunreut.League;
import com.tus.traunreut.ScheduledTask;
import com.tus.traunreut.repository.LeagueRepository;
import com.tus.traunreut.scraper.MatchIDScraper;
import com.tus.traunreut.scraper.MatchScraper;
import com.tus.traunreut.Match;
import com.tus.traunreut.service.schedule.ETaskIds;
import com.tus.traunreut.service.MatchParsingService;
import com.tus.traunreut.repository.MatchRepository;

import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateAllMatchesTask extends ScheduledTask {
    //TODO: move Logger to e.g ScheduledTask so that each task uses the same logger
    private static final Logger databaseLogger = LoggerFactory.getLogger("DATABASE");
    private final MatchRepository matchRepository;
    private MatchParsingService matchParsingService;
    private League league;
    public UpdateAllMatchesTask() {
        this.matchRepository = MatchRepository.getInstance();
        setTaskId(ETaskIds.UPDATE_ALL_MATCHES.getId());
    }



    // This task should run at the start up of the server and once a week
    @Override
    public void execute() {
        // to update all matches in the database
        // Get all leagues, fetch data of all matches (+ MeetingID), update

        try {
            //for each league:
            List<String> leagues = LeagueRepository.findDistinctLeagueNames();
            if (leagues == null || leagues.isEmpty()) {
                databaseLogger.warn("No leagues found in the database. Cannot update matches.");
                return;
            }
            for (String leagueName : leagues) {
                // Step 1: Fetch raw table rows using MatchScraper
                league = LeagueRepository.findByName(leagueName);
                MatchScraper matchScraper = MatchScraper.getInstance(leagueName);
                List<Element> rows = matchScraper.fetchData();
                //rows is the table -> needs to be parse see MatchScraper commented code
                // Step 2: Parse the rows into Match entities
                List<Match> matches = matchParsingService.parseTableData(rows, league);
                if (matches != null && !matches.isEmpty()) {
                    //for each match in matches: get matchID and update data
                    matches.forEach(match -> {
                        // Step 3: Get match ID using MatchIDScraper
                        MatchIDScraper matchIdScraper = new MatchIDScraper(leagueName, match);
                        String matchId = matchIdScraper.fetchData();
                        if (matchId != null && !matchId.isEmpty()) {
                            match.setNuligaMatchId(matchId);
                            // Step 4: Save or update the match in the database
                            matchRepository.saveOrUpdate(match);
                        } else {
                            databaseLogger.warn("No match ID found for match: {} vs {} in league: {}",
                                    match.getHomeTeam().getName(),
                                    match.getGuestTeam().getName(),
                                    leagueName);
                        }
                    });
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
}

package com.tus.traunreut.service.schedule;

import com.tus.traunreut.ScheduledTask;
import com.tus.traunreut.scraper.MatchIDScraper;
import com.tus.traunreut.scraper.MatchScraper;
import com.tus.traunreut.Match;
import com.tus.traunreut.service.schedule.ETaskIds;
import com.tus.traunreut.repository.MatchRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateAllMatchesTask extends ScheduledTask {
    //TODO: move Logger to e.g ScheduledTask so that each task uses the same logger
    private static final Logger databaseLogger = LoggerFactory.getLogger("DATABASE");
    private final MatchRepository matchRepository;
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
            // Step 1: Fetch all matches using MatchScraper
            List<Match> matches = MatchScraper.fetchData();
            //matches is the table -> needs to be parse see MatchScraper commented code
            if (matches != null && !matches.isEmpty()) {
                // Step 2: Update matches in the database
                matches.forEach(match -> matchRepository.saveOrUpdate(match));
            }

            // Step 3: Fetch all match IDs using MatchIDScraper
            List<String> matchIds = MatchIDScraper.fetchData();
            if (matchIds != null && !matchIds.isEmpty()) {
                // Step 4: Update match IDs in the database
                matchIds.forEach(matchId -> matchRepository.saveOrUpdate(matchId));
            }

            databaseLogger.info("UpdateAllMatchesTask executed successfully.");
        } catch (Exception e) {
            databaseLogger.error("Error occurred while executing UpdateAllMatchesTask: ", e);
        }

    }
}

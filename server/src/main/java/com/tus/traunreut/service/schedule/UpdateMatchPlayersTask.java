
package com.tus.traunreut.service.schedule;

import com.tus.traunreut.MatchPlayer;
import com.tus.traunreut.ScheduledTask;
import com.tus.traunreut.scraper.MatchPlayerScraper;
import com.tus.traunreut.Player;
import com.tus.traunreut.repository.PlayerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UpdateMatchPlayersTask extends ScheduledTask {
    //TODO: move Logger to e.g ScheduledTask so that each task uses the same logger
    private static final Logger databaseLogger = LoggerFactory.getLogger("DATABASE");
    private final PlayerRepository playerRepository;
    private final MatchPlayerScraper matchPlayerScraper;

    public UpdateMatchPlayersTask() {
        this.playerRepository = PlayerRepository.getInstance();
        this.matchPlayerScraper = MatchPlayerScraper.getInstance();
        setTaskId(ETaskIds.UPDATE_MATCH_PLAYERS.getId());
    }

    // This task should run in the beginning of a match if the data is available otherwise try every 5 minutes??
    @Override
    public void execute() {
        try {
            // Step 1: Fetch players for the match
            List<MatchPlayer> players = matchPlayerScraper.fetchData();
            if (players != null && !players.isEmpty()) {
                // Step 2: Update players in the database
                players.forEach(player -> playerRepository.saveOrUpdate(player));
                databaseLogger.info("UpdateMatchPlayersTask executed successfully.");
            } else {
                databaseLogger.warn("No player data available for the match. Retrying in 5 minutes...");
                // Retry logic can be implemented here if needed
            }
        } catch (Exception e) {
            databaseLogger.error("Error occurred while executing UpdateMatchPlayersTask: ", e);
        }
    }
}

package com.tus.traunreut.service.schedule.tasks;

import com.tus.traunreut.MatchPlayer;
import com.tus.traunreut.ScheduledTask;
import com.tus.traunreut.scraper.MatchPlayerScraper;
import com.tus.traunreut.repository.PlayerRepository;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Entity
@DiscriminatorValue("2")
public class UpdateMatchPlayersTask extends ScheduledTask {
    //TODO: move Logger to e.g ScheduledTask so that each task uses the same logger
    private static final Logger databaseLogger = LoggerFactory.getLogger("DATABASE");
    private final PlayerRepository playerRepository;
    private final MatchPlayerScraper matchPlayerScraper;


    @Autowired
    public UpdateMatchPlayersTask(PlayerRepository playerRepository, MatchPlayerScraper matchPlayerScraper) {
        this.playerRepository = playerRepository;
        this.matchPlayerScraper = matchPlayerScraper;
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
package com.tus.traunreut.service.schedule.executors.impl;

import com.tus.traunreut.IScraper;
import com.tus.traunreut.Match;
import com.tus.traunreut.MatchPlayer;
import com.tus.traunreut.ScraperFactory;
import com.tus.traunreut.repository.MatchRepository;
import com.tus.traunreut.repository.PlayerRepository;
import com.tus.traunreut.scraper.MatchPlayerScraper;
import com.tus.traunreut.service.schedule.executors.ScheduledTaskExecutor;
import com.tus.traunreut.service.schedule.tasks.UpdateMatchPlayersTask;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UpdateMatchPlayersTaskExecutor implements ScheduledTaskExecutor<UpdateMatchPlayersTask> {
    private static final Logger databaseLogger = LoggerFactory.getLogger("DATABASE");
    private final PlayerRepository playerRepository;
    private final MatchRepository matchRepository;
    private final ScraperFactory scraperFactory;

    @Override
    public void execute(UpdateMatchPlayersTask task) {
        try {
            Optional<Match> match = matchRepository.findById(Long.valueOf(task.getMatchId()));
            if (match.isEmpty())
            {
                databaseLogger.error("Cannot find match with ID {}", task.getMatchId());
                return;
            }
            IScraper<List<MatchPlayer>> matchPlayerScraper = scraperFactory.createMatchPlayerScraper(match.get());
            List<MatchPlayer> players = matchPlayerScraper.fetchData();
            if (players != null && !players.isEmpty()) {
                // players.forEach(playerRepository::saveOrUpdate);
                databaseLogger.info("UpdateMatchPlayersTask executed successfully.");
            } else {
                databaseLogger.warn("No player data available for the match. Retrying in 5 minutes...");
            }
        } catch (Exception e) {
            databaseLogger.error("Error while executing UpdateMatchPlayersTask: ", e);
        }
    }
}

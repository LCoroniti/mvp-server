package com.tus.traunreut.service.schedule.executors.impl;

import com.tus.traunreut.*;
import com.tus.traunreut.repository.MatchPlayerRepository;
import com.tus.traunreut.repository.MatchRepository;
import com.tus.traunreut.scraper.ScraperFactory;
import com.tus.traunreut.service.PlayerService;
import com.tus.traunreut.service.schedule.executors.ScheduledTaskExecutor;
import com.tus.traunreut.service.schedule.tasks.UpdateMatchPlayersTask;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.tus.traunreut.Log.DATABASE;
import static com.tus.traunreut.Log.DB_LOG;


@Service
@RequiredArgsConstructor
public class UpdateMatchPlayersTaskExecutor implements ScheduledTaskExecutor<UpdateMatchPlayersTask> {
    private final MatchPlayerRepository matchPlayerRepository;
    private final PlayerService playerService;
    private final MatchRepository matchRepository;
    private final ScraperFactory scraperFactory;

    @Override
    public void execute(UpdateMatchPlayersTask task) {
        try {
            Optional<Match> optMatch = matchRepository.findById(Long.valueOf(task.getMatchId()));
            if (optMatch.isEmpty()) {
                DB_LOG.error(Log.DATABASE, "Cannot find match with ID {}", task.getMatchId());
                return;
            }
            Match match = optMatch.get();
            IScraper<List<MatchPlayer>> matchPlayerScraper = scraperFactory.createMatchPlayerScraper(match);
            List<MatchPlayer> players = matchPlayerScraper.fetchData();
            if (players != null && !players.isEmpty()) {
                players.forEach(p -> DB_LOG.info(DATABASE, "Create or update player: %s".formatted(p.getPlayer().toString())));
                for (MatchPlayer player : players) {
                    Player persistedPlayer = playerService.createIfNotExist(player.getPlayer().getFirstName(), player.getPlayer().getSurname(), player.getPlayer().getTeam());
                    player.setPlayer(persistedPlayer);
                    player.setId(new MatchPlayerId(match.getId(), persistedPlayer.getId()));
                }
                matchPlayerRepository.saveAll(players);
                DB_LOG.info(Log.DATABASE, "UpdateMatchPlayersTask executed successfully.");
            } else {
                DB_LOG.warn(Log.DATABASE, "No player data available for the match.");
            }
        } catch (Exception e) {
            DB_LOG.error(Log.DATABASE, "Error while executing UpdateMatchPlayersTask: ", e);
        }
    }
}
package com.tus.traunreut.service.schedule.executors.impl;

import com.tus.traunreut.IScraper;
import com.tus.traunreut.League;
import com.tus.traunreut.Match;
import com.tus.traunreut.scraper.DataFetchException;
import com.tus.traunreut.scraper.ScraperFactory;
import com.tus.traunreut.service.MatchService;
import com.tus.traunreut.service.schedule.executors.ScheduledTaskExecutor;
import com.tus.traunreut.service.schedule.tasks.GetNuLigaIdTask;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.tus.traunreut.Log.INTERNAL;
import static com.tus.traunreut.Log.INTERNAL_LOG;

@RequiredArgsConstructor
public class GetNuLigaTaskExecutor implements ScheduledTaskExecutor<GetNuLigaIdTask> {
    private final ScraperFactory scraperFactory;
    private final MatchService matchService;

    @Override
    public void execute(GetNuLigaIdTask task) {
        if (task.getMatchId() == null) {
            INTERNAL_LOG.error(INTERNAL, "No match ID present for GetNuLigaTask");
            return;
        }
        Optional<Match> optMatch = matchService.getMatchById(Long.valueOf(task.getMatchId()));
        if (optMatch.isEmpty()) {
            INTERNAL_LOG.error(INTERNAL, "No match found for ID {} in GetNuLigaTask", task.getMatchId());
            return;
        }
        Match match = optMatch.get();
        League league = match.getHomeTeam().getLeague();
        IScraper<String> matchIdScraper = scraperFactory.createMatchIdScraper(league, match);
        try {
            String meetingId = matchIdScraper.fetchData();
            if (meetingId == null) {
                INTERNAL_LOG.error("No meeting ID found for match with ID {} during GetNuLigaTask", task.getMatchId());
            }
            match.setNuligaMatchId(meetingId);
            matchService.updateMatch(match.getId(), match);
        } catch (DataFetchException e) {
            // Logging already done in Scraper
        }
    }
}

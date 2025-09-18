package com.tus.traunreut.service.schedule.applicationlistener;

import com.tus.traunreut.Match;
import com.tus.traunreut.events.MatchUpdateEvent;
import com.tus.traunreut.service.schedule.TaskSchedulingService;
import com.tus.traunreut.service.schedule.tasks.GetNuLigaIdTask;
import com.tus.traunreut.service.schedule.tasks.UpdateAllMatchesTask;
import com.tus.traunreut.service.schedule.tasks.UpdateMatchPlayersTask;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OnMatchUpdatedScheduler {
    private final TaskSchedulingService schedulingService;

    @EventListener
    public void handleMatchUpdateEvent(MatchUpdateEvent event) {
        Match match = event.getMatch();
        schedulingService.cancelAllTasksForMatch(match);

        LocalDateTime matchDate = match.getMatchDate();
        String matchId = match.getId().toString();

        GetNuLigaIdTask getNuLigaIdTask = new GetNuLigaIdTask(matchDate.minusMinutes(5), matchId);
        UpdateMatchPlayersTask updateMatchPlayersTask = new UpdateMatchPlayersTask(matchDate, matchId);
        UpdateAllMatchesTask updateAllMatchesTask = new UpdateAllMatchesTask(matchDate.plusHours(2));
        schedulingService.addTasks(List.of(getNuLigaIdTask, updateMatchPlayersTask, updateAllMatchesTask));
    }
}

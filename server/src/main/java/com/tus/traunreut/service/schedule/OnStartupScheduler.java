package com.tus.traunreut.service.schedule;

import com.tus.traunreut.DateTimeUtil;
import com.tus.traunreut.Markers;
import com.tus.traunreut.ScheduledTask;
import com.tus.traunreut.events.InitializeScheduledTasksEvent;
import com.tus.traunreut.events.PrepareScheduledTasksEvent;
import com.tus.traunreut.repository.LeagueRepository;
import com.tus.traunreut.repository.ScheduledTaskRepository;
import com.tus.traunreut.service.schedule.tasks.UpdateAllMatchesTask;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.tus.traunreut.Markers.DB_LOG;

@Service
@RequiredArgsConstructor
public class OnStartupScheduler {
    private final ScheduledTaskRepository scheduledTaskRepository;
    private final LeagueRepository leagueRepository;

    @EventListener
    public void handleInitializeScheduledTasks(PrepareScheduledTasksEvent event) {
        LocalDateTime executionTime = LocalDateTime.now().plusSeconds(10);
        UpdateAllMatchesTask task = new UpdateAllMatchesTask(executionTime);
        scheduledTaskRepository.save(task);
        DB_LOG.info(Markers.DATABASE, "Saved UpdateAllMatchesTask in database to be executed at {}", DateTimeUtil.formatDate(executionTime));
    }
}

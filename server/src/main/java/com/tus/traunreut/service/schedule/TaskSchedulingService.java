package com.tus.traunreut.service.schedule;

import com.tus.traunreut.ScheduledTask;
import com.tus.traunreut.repository.ScheduledTaskRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskSchedulingService {
    private static final Logger LOGGER = LoggerFactory.getLogger("CONSOLE");

    private final ScheduledTaskRepository repository;
    private final TaskScheduler taskScheduler;

    // Keep track of scheduled futures for canceling
    private final Map<Long, ScheduledTaskInfo> scheduledTasks = new ConcurrentHashMap<>();

    // TODO: When a service is added to update/add scheduled tasks on startup, create a custom ApplicationEvent and use @EventListener public void onTasksUpdated(TasksUpdatedEvent event) ... (if the custom event is named TasksUpdatedEvent)
    @PostConstruct
    public void init() {
        List<ScheduledTask> tasks = repository.findByExecutionTimeAfter(LocalDateTime.now());
        LOGGER.info("Scheduling {} tasks on startup", tasks.size());

        for (ScheduledTask task : tasks) {
            scheduleTask(task);
        }
    }

    /**
     * Schedule a single task in memory
     */
    private void scheduleTask(ScheduledTask task) {
        Instant executionInstant = task.getExecutionTime()
                .atZone(ZoneId.of("Europe/Berlin"))
                .toInstant();

        ScheduledFuture<?> future = taskScheduler.schedule(
                () -> executeAndRemove(task),
                executionInstant
        );
        scheduledTasks.put(task.getId(), new ScheduledTaskInfo(future, executionInstant));

        LOGGER.info("Task {} scheduled to run at {}", task.getId(), task.getExecutionTime());
    }

    @Scheduled(fixedDelayString = "30000")
    public void updatedScheduledTasksFromDatabase() {
        List<ScheduledTask> dbTasks = repository.findAllPendingTasks(LocalDateTime.now());
        Set<Long> dbTaskIds = dbTasks.stream()
                .map(ScheduledTask::getId)
                .collect(Collectors.toSet());

        // Cancel tasks no longer in DB
        for (Long scheduledTaskId : new HashSet<>(scheduledTasks.keySet())) {
            if (!dbTaskIds.contains(scheduledTaskId)) {
                ScheduledTaskInfo info = scheduledTasks.remove(scheduledTaskId);
                if (info != null) {
                    info.future().cancel(false);
                    LOGGER.info("Cancelled scheduled task {} because it was removed from DB", scheduledTaskId);
                }
            }
        }

        Instant now = Instant.now();
        ZoneId zoneId = ZoneId.of("Europe/Berlin");

        for (ScheduledTask task : dbTasks) {
            ScheduledTaskInfo scheduledInfo = scheduledTasks.get(task.getId());

            Instant newExecutionInstant = task.getExecutionTime().atZone(zoneId).toInstant();

            if (scheduledInfo == null) {
                // Not scheduled yet
                if (newExecutionInstant.isBefore(now)) {
                    LOGGER.warn("Task {} execution time in past, executing immediately", task.getId());
                    executeAndRemove(task);
                } else {
                    scheduleTask(task);
                }
            } else {
                // Task is scheduled: check if execution time changed
                Instant scheduledExecutionInstant = scheduledInfo.executionTime();

                if (!newExecutionInstant.equals(scheduledExecutionInstant)) {
                    // Execution time changed -> cancel old and reschedule
                    scheduledInfo.future().cancel(false);
                    LOGGER.info("Rescheduling task {} due to execution time update", task.getId());
                    scheduleTask(task);
                }
                // else do nothing, already scheduled with correct time
            }
        }
    }

    /**
     * Execute the task and remove it from DB
     */
    @Transactional
    public void executeAndRemove(ScheduledTask task) {
        try {
            LOGGER.info("Executing task {}", task.getId());
            task.execute();
            repository.deleteById(task.getId());
            scheduledTasks.remove(task.getId());
        } catch (Exception e) {
            LOGGER.error("Failed to execute task {}", task.getId(), e);
        }
    }

    /**
     * Adds the {@link ScheduledTask} to the repository and schedules the task in memory.
     */
    public ScheduledTask addTask(ScheduledTask task) {
        task = repository.save(task);
        scheduleTask(task);
        return task;
    }

    /**
     * Cancels a scheduled task by its ID if it is currently scheduled in memory.
     *
     * @param taskId the ID of the scheduled task to cancel
     * @return true if the task was found and cancelled, false otherwise
     */
    public boolean cancelScheduledTask(Long taskId) {
        ScheduledTaskInfo info = scheduledTasks.remove(taskId);
        if (info != null) {
            boolean cancelled = info.future().cancel(false);
            if (cancelled) {
                LOGGER.info("Cancelled scheduled task {}", taskId);
            } else {
                LOGGER.warn("Failed to cancel scheduled task {}", taskId);
            }
            return cancelled;
        } else {
            LOGGER.warn("No scheduled task found with id {}", taskId);
            return false;
        }
    }
}

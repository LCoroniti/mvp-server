package com.tus.traunreut.service.schedule;

import com.tus.traunreut.Match;
import com.tus.traunreut.ScheduledTask;
import com.tus.traunreut.events.InitializeScheduledTasksEvent;
import com.tus.traunreut.repository.ScheduledTaskRepository;
import com.tus.traunreut.service.schedule.executors.ScheduledTaskExecutor;
import com.tus.traunreut.service.schedule.executors.TaskExecutorRegistry;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
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

import static com.tus.traunreut.Log.*;

@Service
@RequiredArgsConstructor
public class TaskSchedulingService {
    private final ScheduledTaskRepository repository;
    private final TaskScheduler taskScheduler;
    private final TaskExecutorRegistry taskExecutorRegistry;

    // Keep track of scheduled futures for canceling
    private final Map<Long, ScheduledTaskInfo> scheduledTasks = new ConcurrentHashMap<>();

    @EventListener
    public void handleInitializeScheduledTasks(InitializeScheduledTasksEvent event) {
        List<ScheduledTask> tasks = repository.findByExecutionTimeAfter(LocalDateTime.now());
        DB_LOG.info(DATABASE, "Scheduling {} tasks on startup", tasks.size());

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

        DB_LOG.info(DATABASE, "{} (Task ID: {}) scheduled to run at {}", task.getClass().getSimpleName(), task.getId(), task.getExecutionTime());
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
                    DB_LOG.info(DATABASE, "Cancelled scheduled task {} because it was removed from DB", scheduledTaskId);
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
                    DB_LOG.info(DATABASE, "Task {} execution time in past, executing immediately", task.getId());
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
                    DB_LOG.info(DATABASE, "Rescheduling task {} due to execution time update", task.getId());
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
            ScheduledTaskExecutor<ScheduledTask> executor = taskExecutorRegistry.getExecutor(task);
            if (executor != null) {
                INTERNAL_LOG.info(INTERNAL, "Executing task {}", task.getId());
                executor.execute(task);
            } else {
                INTERNAL_LOG.info(INTERNAL, "No executor found for task {}", task.getId());
            }
            repository.deleteById(task.getId());
            scheduledTasks.remove(task.getId());
        } catch (Exception e) {
            INTERNAL_LOG.info(INTERNAL, "Failed to execute task {}", task.getId(), e);
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
     * Adds multiple {@link ScheduledTask}s to the repository and schedules them in memory.
     *
     * @param tasks The list of tasks to add.
     * @return The list of saved tasks with their generated IDs.
     */
    public List<ScheduledTask> addTasks(List<ScheduledTask> tasks) {
        List<ScheduledTask> savedTasks = repository.saveAll(tasks);
        for (ScheduledTask task : savedTasks) {
            scheduleTask(task);
        }
        return savedTasks;
    }

    /**
     * Cancels a scheduled task by its ID if it is currently scheduled in memory.
     *
     * @param taskId the ID of the scheduled task to cancel
     * @return true if the task was found and cancelled, false otherwise
     */
    @Transactional
    public boolean cancelScheduledTask(Long taskId) {
        ScheduledTaskInfo info = scheduledTasks.remove(taskId);
        if (info != null) {
            boolean cancelled = info.future().cancel(false);
            if (cancelled) {
                repository.deleteById(taskId);
                DB_LOG.info(DATABASE, "Cancelled scheduled task {} and removed from database", taskId);
            } else {
                DB_LOG.error(DATABASE, "Failed to cancel scheduled task {}. Still present in database!", taskId);
            }
            return cancelled;
        } else {
            DB_LOG.info(DATABASE, "No scheduled task found with id {}", taskId);
            return false;
        }
    }

    Map<Long, ScheduledTaskInfo> getScheduledTasks() {
        return Collections.unmodifiableMap(scheduledTasks);
    }

    public List<ScheduledTask> getAllScheduledTasks() {
        return repository.findAll();
    }

    /**
     * Finds all scheduled tasks for a given match, cancels them from the in-memory scheduler,
     * and removes them from the database.
     *
     * @param match The match for which all scheduled tasks should be cancelled.
     */
    @Transactional
    public void cancelAllTasksForMatch(Match match) {
        String matchId = String.valueOf(match.getId());
        List<ScheduledTask> tasksForMatch = repository.findAllByMatchId(matchId);

        if (tasksForMatch.isEmpty()) {
            DB_LOG.info(DATABASE, "No scheduled tasks found to cancel for match id {}", matchId);
            return;
        }

        DB_LOG.info(DATABASE, "Cancelling {} tasks for match id {}", tasksForMatch.size(), matchId);

        List<Long> taskIds = new ArrayList<>();
        for (ScheduledTask task : tasksForMatch) {
            Long taskId = task.getId();
            taskIds.add(taskId);

            // Cancel any in-memory scheduled future for the task
            ScheduledTaskInfo info = scheduledTasks.remove(taskId);
            if (info != null) {
                info.future().cancel(false);
            }
        }

        // Delete all tasks from the database in a single, efficient batch operation
        repository.deleteAllByIdInBatch(taskIds);
        DB_LOG.info(DATABASE, "Removed {} tasks from database for match id {}", tasksForMatch.size(), matchId);
    }
}

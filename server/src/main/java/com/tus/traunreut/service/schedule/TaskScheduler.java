package com.tus.traunreut.service.schedule;

import com.tus.traunreut.DateTimeUtil;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class TaskScheduler {
    private static final Logger LOGGER = LoggerFactory.getLogger("CONSOLE");

    private static TaskScheduler instance;
    private final ScheduledExecutorService executorService;
    private final Map<Task, ScheduledFuture<?>> scheduledTasks;

    private TaskScheduler() {
        this.executorService = Executors.newScheduledThreadPool(5);
        this.scheduledTasks = new HashMap<>();
    }

    public static synchronized TaskScheduler getInstance() {
        if (instance == null) {
            instance = new TaskScheduler();
        }
        return instance;
    }

    /**
     * Schedule a Task that will be executed at the specified time.
     * If a Task with the same ID is already scheduled, the Task will
     * be overwritten by the new one.
     *
     * @param task to execute
     * @param executionTime of the task
     */
    public void scheduleTask(Task task, LocalDateTime executionTime) {
        synchronized (scheduledTasks)
        {
            long delay = Duration.between(DateTimeUtil.nowGerman(), executionTime).toMillis();
            if (delay > 0) {
                ScheduledFuture<?> scheduledTask = executorService.schedule(() -> {
                    task.run();
                    removeTask(task);
                }, delay, TimeUnit.MILLISECONDS);
                if (scheduledTasks.containsKey(task))
                {
                    LOGGER.info("Scheduled task will be overridden. Task id = %s".formatted(task.getId()));
                }
                scheduledTasks.put(task, scheduledTask);
            }
        }
    }

    /**
     * Remove the task from the List of Tasks. If the Task is in the future and was not executed until now,
     * the Task will not be executed in the future.
     */
    public void removeTask(Task task)
    {
        synchronized (scheduledTasks) {
            ScheduledFuture<?> scheduledTask = scheduledTasks.getOrDefault(task, null);
            if (scheduledTask != null)
            {
                scheduledTask.cancel(false);
                scheduledTasks.remove(task);
            }
        }
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }
}

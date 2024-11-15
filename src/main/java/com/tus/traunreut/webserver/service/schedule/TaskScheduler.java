package com.tus.traunreut.webserver.service.schedule;

import com.tus.traunreut.webserver.util.DateTimeUtil;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class TaskScheduler {
    private static TaskScheduler instance;
    private final ScheduledExecutorService executorService;

    private TaskScheduler() {
        this.executorService = Executors.newScheduledThreadPool(5);
    }

    public static synchronized TaskScheduler getInstance() {
        if (instance == null) {
            instance = new TaskScheduler();
        }
        return instance;
    }

    public void scheduleTask(Task task, LocalDateTime executionTime) {
        long delay = Duration.between(DateTimeUtil.nowGerman(), executionTime).toMillis();
        if (delay > 0) {
            executorService.schedule(task, delay, TimeUnit.MILLISECONDS);
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

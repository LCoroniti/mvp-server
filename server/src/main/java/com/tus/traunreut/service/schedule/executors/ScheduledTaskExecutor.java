package com.tus.traunreut.service.schedule.executors;

import com.tus.traunreut.ScheduledTask;

public interface ScheduledTaskExecutor <T extends ScheduledTask> {
    void execute(T task);
}

package com.tus.traunreut.service.schedule;

import com.tus.traunreut.ScheduledTask;

public class UpdateAllMatchesTask extends ScheduledTask {
    public UpdateAllMatchesTask() {
        setTaskId(ETaskIds.UPDATE_ALL_MATCHES.getId());
    }

    @Override
    public void execute() {
        // Get all leagues, fetch data, update
    }
}

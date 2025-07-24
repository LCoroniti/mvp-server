package com.tus.traunreut.service.schedule.executors.impl;

import com.tus.traunreut.service.schedule.executors.ScheduledTaskExecutor;
import com.tus.traunreut.service.schedule.tasks.UpdateSingleMatchTask;

public class UpdateSingleMatchTaskExecutor implements ScheduledTaskExecutor<UpdateSingleMatchTask> {
    @Override
    public void execute(UpdateSingleMatchTask task) {
        // get match by ID, fetch data of the match, update
        // This task should run after a match to get the final result and during the match to update score??

        // Use MatchScraper to fetch the match data
        // -> returns List of Matches
        // -> get match by ID from List
        // -> set/update data of received match to database
    }
}

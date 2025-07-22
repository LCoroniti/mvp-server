package com.tus.traunreut.service.schedule.tasks;

import com.tus.traunreut.ScheduledTask;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("3")
public class UpdateSingleMatchTask extends ScheduledTask {
    @Override
    public void execute() {
        // get match by ID, fetch data of the match, update
        // This task should run after a match to get the final result and during the match to update score??

        // Use MatchScraper to fetch the match data
        // -> returns List of Matches
        // -> get match by ID from List
        // -> set/update data of received match to database

    }
}

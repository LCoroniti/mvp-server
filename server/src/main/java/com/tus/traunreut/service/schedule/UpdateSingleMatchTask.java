UpdateAllMatchesTaskpackage com.tus.traunreut.service.schedule;

import com.tus.traunreut.ScheduledTask;
import com.tus.traunreut.service.schedule.ETaskIds;

public class UpdateSingleMatchTask extends ScheduledTask {
    public UpdateSingleMatchTask() {
        setTaskId(ETaskIds.UPDATE_SINGLE_MATCH.getId());
    }

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

package com.tus.traunreut.service.schedule.tasks;

import com.tus.traunreut.ScheduledTask;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("1")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateAllMatchesTask extends ScheduledTask {
    public UpdateAllMatchesTask(LocalDateTime executionTime, String matchId) {
        super(executionTime);
        setMatchId(matchId);
    }

    public UpdateAllMatchesTask(LocalDateTime executionTime) {
        super(executionTime);
    }

    @Override
    public String getTaskName() {
        return "Update all Matches";
    }
}

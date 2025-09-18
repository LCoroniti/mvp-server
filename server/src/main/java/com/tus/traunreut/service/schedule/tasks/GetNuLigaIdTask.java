package com.tus.traunreut.service.schedule.tasks;

import com.tus.traunreut.ScheduledTask;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("4")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetNuLigaIdTask extends ScheduledTask {
    public GetNuLigaIdTask(LocalDateTime executionTime, String matchId) {
        super(executionTime);
        setMatchId(matchId);
    }

    @Override
    public String getTaskName() {
        return "Get NuLiga Match ID";
    }
}

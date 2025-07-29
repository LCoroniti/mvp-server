package com.tus.traunreut.service.schedule.tasks;

import com.tus.traunreut.ScheduledTask;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("2")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateMatchPlayersTask extends ScheduledTask {
    public UpdateMatchPlayersTask(LocalDateTime executionTime) {
        super(executionTime);
    }

    @Override
    public String getTaskName() {
        return "Update Match Players";
    }
}
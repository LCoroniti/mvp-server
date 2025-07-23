package com.tus.traunreut.service.schedule.tasks;

import com.tus.traunreut.ScheduledTask;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("3")
public class UpdateSingleMatchTask extends ScheduledTask {
    public UpdateSingleMatchTask(LocalDateTime executionTime) {
        super(executionTime);
    }
}

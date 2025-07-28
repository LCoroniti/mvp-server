package com.tus.traunreut.service.schedule.tasks;

import com.tus.traunreut.ScheduledTask;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@DiscriminatorValue("3")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UpdateSingleMatchTask extends ScheduledTask {
    public UpdateSingleMatchTask(LocalDateTime executionTime) {
        super(executionTime);
    }
}

package com.tus.traunreut.service.schedule.tasks;

import com.tus.traunreut.ScheduledTask;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@DiscriminatorValue("4")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GetNuLigaIdTask extends ScheduledTask {
    @Override
    public String getTaskName() {
        return "Get NuLiga ID";
    }
}

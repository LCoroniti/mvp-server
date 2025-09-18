package com.tus.traunreut.dto;

import com.tus.traunreut.Match;
import com.tus.traunreut.ScheduledTask;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ScheduledTaskDto {
    private final String id;
    private final LocalDateTime executionTime;
    private final boolean isMatchTask;
    private final String taskName;
    private String matchId;
    private String homeTeam;
    private String guestTeam;
    private LocalDateTime matchDateTime;

    public ScheduledTaskDto(ScheduledTask task, Match match) {
        id = task.getId().toString();
        taskName = task.getTaskName();
        executionTime = task.getExecutionTime();
        isMatchTask = match != null;
        if (isMatchTask) {
            matchId = match.getId().toString();
            homeTeam = match.getHomeTeam().getName();
            guestTeam = match.getGuestTeam().getName();
            matchDateTime = match.getMatchDate();
        }
    }
}

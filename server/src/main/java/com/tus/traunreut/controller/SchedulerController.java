package com.tus.traunreut.controller;

import com.tus.traunreut.Match;
import com.tus.traunreut.ScheduledTask;
import com.tus.traunreut.dto.ScheduledTaskDto;
import com.tus.traunreut.service.MatchService;
import com.tus.traunreut.service.schedule.TaskSchedulingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/scheduler")
@RequiredArgsConstructor
public class SchedulerController {
    private final TaskSchedulingService schedulingService;
    private final MatchService matchService;

    @GetMapping
    public ResponseEntity<List<ScheduledTaskDto>> getAllScheduledTasks() {
        List<ScheduledTask> scheduledTasks = schedulingService.getAllScheduledTasks();

        Set<Long> matchIds = scheduledTasks.stream()
                .map(ScheduledTask::getMatchId)
                .filter(Objects::nonNull)
                .map(Long::valueOf)
                .collect(Collectors.toSet());

        List<Match> matches = matchService.getMatchesByIds(matchIds);
        Map<Long, Match> matchMap = matches.stream()
                .collect(Collectors.toMap(Match::getId, Function.identity()));

        List<ScheduledTaskDto> responseDtoList = scheduledTasks.stream()
                .map(task -> {
                    Match match = null;
                    if (task.getMatchId() != null) {
                        match = matchMap.get(Long.valueOf(task.getMatchId()));
                    }
                    return new ScheduledTaskDto(task, match);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(responseDtoList);
    }
}

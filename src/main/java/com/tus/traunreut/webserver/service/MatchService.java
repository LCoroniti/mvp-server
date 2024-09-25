package com.tus.traunreut.webserver.service;

import com.tus.traunreut.webserver.model.Match;
import com.tus.traunreut.webserver.repository.MatchRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;

@Service
public class MatchService {
    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public Optional<Match> getNextMatch() {
        return matchRepository.findTopByMatchDateAfterOrderByMatchDateAsc(LocalDateTime.now());
    }

    @Transactional
    public List<Match> getAllMatchesThisWeekend() {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime currentSaturday;
        if (today.get(ChronoField.DAY_OF_WEEK) == DayOfWeek.SUNDAY.getValue()) {
            currentSaturday = today.with(TemporalAdjusters.previous(DayOfWeek.SATURDAY))
                    .toLocalDate()
                    .atStartOfDay();
        } else {
            currentSaturday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SATURDAY))
                    .toLocalDate()
                    .atStartOfDay();
        }
        LocalDateTime currentSunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                .toLocalDate()
                .atTime(LocalTime.MAX);
        return matchRepository.findMatchesForCurrentWeekend(currentSaturday, currentSunday);
    }
}

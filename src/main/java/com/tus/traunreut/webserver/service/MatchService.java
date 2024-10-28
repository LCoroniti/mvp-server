package com.tus.traunreut.webserver.service;

import com.tus.traunreut.webserver.model.Match;
import com.tus.traunreut.webserver.repository.MatchRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public Optional<Match> getMatchById(Long id) {
        return matchRepository.findById(id);
    }

    @Transactional
    public List<Match> getAllMatchesCurrentWeek() {
        LocalDateTime today = LocalDateTime.now().minusHours(1);
        LocalDateTime currentSunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                .toLocalDate()
                .atTime(LocalTime.MAX);
        return matchRepository.findMatchesInTimeRange(today, currentSunday);
    }

}

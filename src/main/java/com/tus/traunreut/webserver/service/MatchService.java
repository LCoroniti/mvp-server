package com.tus.traunreut.webserver.service;

import com.tus.traunreut.webserver.model.Match;
import com.tus.traunreut.webserver.repository.MatchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MatchService {
    private final MatchRepository matchRepository;

    public MatchService(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public Match saveMatch(Match match) {
        return matchRepository.save(match);
    }

    public Match getMatchById(String id) {
        return matchRepository.findById(id).orElseThrow(() -> new RuntimeException("Match not found"));
    }
}

package com.tus.traunreut.webserver.service;

import com.tus.traunreut.webserver.model.League;
import com.tus.traunreut.webserver.repository.LeagueRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeagueService {
    private final LeagueRepository leagueRepository;

    public LeagueService(LeagueRepository leagueRepository){
        this.leagueRepository = leagueRepository;
    }

    public List<String> getDistinctLeagueNames() {
        return leagueRepository.findDistinctLeagueNames();
    }
}

package com.tus.traunreut.service;

import com.tus.traunreut.League;
import com.tus.traunreut.repository.LeagueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LeagueService {
    private final LeagueRepository leagueRepository;

    public LeagueService(LeagueRepository leagueRepository){
        this.leagueRepository = leagueRepository;
    }

    public List<String> getDistinctLeagueNames() {
        return leagueRepository.findDistinctLeagueNames();
    }

    public List<League> getLeagues() {
        return leagueRepository.findAll();
    }

    public League updateLeague(Long id, League updatedLeague)
    {
        League existingEntry = leagueRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("League not found with id " + id));

        existingEntry.setName(updatedLeague.getName());
        existingEntry.setGroupdId(updatedLeague.getGroupdId());
        existingEntry.setSeason(updatedLeague.getSeason());
        existingEntry.setLeaguePlanUrl(updatedLeague.getLeaguePlanUrl());

        return leagueRepository.save(updatedLeague);
    }

    public League createLeague(League league) {
        league.setId(null);
        return leagueRepository.save(league);
    }
}

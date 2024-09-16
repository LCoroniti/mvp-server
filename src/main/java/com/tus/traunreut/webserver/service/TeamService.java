package com.tus.traunreut.webserver.service;

import com.tus.traunreut.webserver.model.Club;
import com.tus.traunreut.webserver.model.Team;
import com.tus.traunreut.webserver.repository.ClubRepository;
import com.tus.traunreut.webserver.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final ClubRepository clubRepository;

    public TeamService(TeamRepository teamRepository, ClubRepository clubRepository) {
        this.teamRepository = teamRepository;
        this.clubRepository = clubRepository;
    }

    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    public Team saveTeamForClub(Team team, String clubName) {
        Club club = clubRepository.findByName(clubName);
        if (club == null) {
            throw new NoSuchElementException("Club not found");
        }
        team.setClubId(club.getId());
        return teamRepository.save(team);
    }

    public Team getTeamById(String id) {
        return teamRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Team not found"));
    }
}

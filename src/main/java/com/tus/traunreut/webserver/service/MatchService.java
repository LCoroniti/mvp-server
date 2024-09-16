package com.tus.traunreut.webserver.service;

import com.tus.traunreut.webserver.dto.ClubDTO;
import com.tus.traunreut.webserver.dto.MatchDTO;
import com.tus.traunreut.webserver.dto.TeamDTO;
import com.tus.traunreut.webserver.model.Club;
import com.tus.traunreut.webserver.model.Match;
import com.tus.traunreut.webserver.model.Team;
import com.tus.traunreut.webserver.repository.ClubRepository;
import com.tus.traunreut.webserver.repository.MatchRepository;
import com.tus.traunreut.webserver.repository.PlayerRepository;
import com.tus.traunreut.webserver.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MatchService {
    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final ClubRepository clubRepository;
    private final PlayerRepository playerRepository;

    public MatchService(@Autowired MatchRepository matchRepository, @Autowired TeamRepository teamRepository, @Autowired ClubRepository clubRepository, @Autowired PlayerRepository playerRepository) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.clubRepository = clubRepository;
        this.playerRepository = playerRepository;
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

    @Transactional(readOnly = true)
    public MatchDTO getNextMatch() {
        return convertToMatchDTO(matchRepository.findFirstByOrderByMatchDateAsc());
    }

    private MatchDTO convertToMatchDTO(Match match) {
        // Fetch teams
        Team homeTeam = teamRepository.findById(match.getHomeTeamId()).orElse(null);
        Team awayTeam = teamRepository.findById(match.getAwayTeamId()).orElse(null);

        // Fetch clubs
        Club homeClub = homeTeam != null ? clubRepository.findById(homeTeam.getClubId()).orElse(null) : null;
        Club awayClub = awayTeam != null ? clubRepository.findById(awayTeam.getClubId()).orElse(null) : null;

        // Convert to DTO
        MatchDTO dto = new MatchDTO();
        dto.setId(match.getId());
        dto.setHomeTeamId(match.getHomeTeamId());
        dto.setAwayTeamId(match.getAwayTeamId());
        dto.setMatchDate(match.getMatchDate());
        dto.setPlayers(playerRepository.findAllById(match.getPlayerIds()));
        dto.setVotes(match.getVotes());

        if (homeTeam != null && homeClub != null) {
            ClubDTO homeClubDTO = new ClubDTO(homeClub.getId(), homeClub.getName(), homeClub.getImageUrl());
            TeamDTO homeTeamDTO = new TeamDTO(homeTeam.getId(), homeTeam.getName(), homeClubDTO);
            dto.setHomeTeam(homeTeamDTO);
        }

        if (awayTeam != null && awayClub != null) {
            ClubDTO awayClubDTO = new ClubDTO(awayClub.getId(), awayClub.getName(), awayClub.getImageUrl());
            TeamDTO awayTeamDTO = new TeamDTO(awayTeam.getId(), awayTeam.getName(), awayClubDTO);
            dto.setAwayTeam(awayTeamDTO);
        }

        return dto;
    }

    public Match addPlayersToMatch(String id, List<String> playerIds) {
        Match match = matchRepository.findById(id).orElseThrow();
        if (match.getPlayerIds() == null) {
            match.setPlayerIds(playerIds);
        } else {
            match.getPlayerIds().addAll(playerIds);
        }
        return matchRepository.save(match);
    }
}

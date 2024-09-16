package com.tus.traunreut.webserver.dto;

import com.tus.traunreut.webserver.model.Player;
import com.tus.traunreut.webserver.model.Vote;

import java.time.LocalDateTime;
import java.util.List;

public class MatchDTO {
    private String id;
    private String homeTeamId;
    private String awayTeamId;
    private LocalDateTime matchDate;
    private List<Player> players;
    private List<Vote> votes;
    private TeamDTO homeTeam;
    private TeamDTO awayTeam;

    // Constructors
    public MatchDTO(String id, String homeTeamId, String awayTeamId, LocalDateTime matchDate, List<Player> players, List<Vote> votes, TeamDTO homeTeam, TeamDTO awayTeam) {
        this.id = id;
        this.homeTeamId = homeTeamId;
        this.awayTeamId = awayTeamId;
        this.matchDate = matchDate;
        this.players = players;
        this.votes = votes;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public MatchDTO() {}

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getHomeTeamId() {
        return homeTeamId;
    }

    public void setHomeTeamId(String homeTeamId) {
        this.homeTeamId = homeTeamId;
    }

    public String getAwayTeamId() {
        return awayTeamId;
    }

    public void setAwayTeamId(String awayTeamId) {
        this.awayTeamId = awayTeamId;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }

    public TeamDTO getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(TeamDTO homeTeam) {
        this.homeTeam = homeTeam;
    }

    public TeamDTO getAwayTeam() {
        return awayTeam;
    }

    public void setAwayTeam(TeamDTO awayTeam) {
        this.awayTeam = awayTeam;
    }
}

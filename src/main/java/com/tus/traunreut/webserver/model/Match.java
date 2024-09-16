package com.tus.traunreut.webserver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "matches")
public class Match {
    @Id
    private String id;
    private String homeTeamId;
    private String awayTeamId;
    private LocalDateTime matchDate;
    private List<String> playerIds;
    private List<Vote> votes;

    public Match(String id, String homeTeam, String awayTeam, LocalDateTime matchDate, List<String> players, List<Vote> votes) {
        this.id = id;
        this.homeTeamId = homeTeam;
        this.awayTeamId = awayTeam;
        this.matchDate = matchDate;
        this.playerIds = players;
        this.votes = votes;
    }

    public Match() {
    }

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

    public List<String> getPlayerIds() {
        return playerIds;
    }

    public void setPlayerIds(List<String> playerIds) {
        this.playerIds = playerIds;
    }

    public List<Vote> getVotes() {
        return votes;
    }

    public void setVotes(List<Vote> votes) {
        this.votes = votes;
    }
}

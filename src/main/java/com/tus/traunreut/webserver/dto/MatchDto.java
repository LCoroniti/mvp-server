package com.tus.traunreut.webserver.dto;

import com.tus.traunreut.webserver.model.Gender;
import com.tus.traunreut.webserver.model.League;
import com.tus.traunreut.webserver.model.Match;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MatchDto {
    private String id;
    private String homeTeamName;
    private String guestTeamName;
    private String homeTeamLogoBase64;
    private String guestTeamLogoBase64;
    private LocalDateTime matchDate;
    private List<PlayerDto> players;
    private Gender gender;
    private League league;

    public MatchDto(Match match, String homeTeamLogoBase64, String guestTeamLogoBase64) {
        this.id = match.getId().toString();
        this.homeTeamName = match.getHomeTeam().getName();
        this.guestTeamName = match.getGuestTeam().getName();
        this.homeTeamLogoBase64 = homeTeamLogoBase64;
        this.guestTeamLogoBase64 = guestTeamLogoBase64;
        this.matchDate = match.getMatchDate();
        this.players = (match.getPlayers() != null)
                ? match.getPlayers().stream().map(PlayerDto::new).toList()
                : new ArrayList<>();
        this.gender = match.getHomeTeam().getGender();
        this.league = match.getHomeTeam().getLeague();
    }

    public String getHomeTeamName() {
        return homeTeamName;
    }

    public void setHomeTeamName(String homeTeamName) {
        this.homeTeamName = homeTeamName;
    }

    public String getGuestTeamName() {
        return guestTeamName;
    }

    public void setGuestTeamName(String guestTeamName) {
        this.guestTeamName = guestTeamName;
    }

    public String getHomeTeamLogoBase64() {
        return homeTeamLogoBase64;
    }

    public void setHomeTeamLogoBase64(String homeTeamLogoBase64) {
        this.homeTeamLogoBase64 = homeTeamLogoBase64;
    }

    public String getGuestTeamLogoBase64() {
        return guestTeamLogoBase64;
    }

    public void setGuestTeamLogoBase64(String guestTeamLogoBase64) {
        this.guestTeamLogoBase64 = guestTeamLogoBase64;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public List<PlayerDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDto> players) {
        this.players = players;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public League getLeague() {
        return league;
    }

    public void setLeague(League league) {
        this.league = league;
    }
}

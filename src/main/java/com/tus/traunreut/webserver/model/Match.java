package com.tus.traunreut.webserver.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "matches")
public class Match {

    @Id
    @Column(name = "match_id")
    private Long id;

    @Column(name = "start_timestamp")
    private LocalDateTime matchDate;

    @ManyToOne
    @JoinColumn(name = "home_team_id", referencedColumnName = "team_id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "guest_team_id", referencedColumnName = "team_id")
    private Team guestTeam;

    @OneToMany(mappedBy = "match", fetch = FetchType.EAGER)
    private List<MatchPlayer> players;

    public Match(Long id, LocalDateTime matchDate, Team homeTeam, Team guestTeam) {
        this.id = id;
        this.matchDate = matchDate;
        this.homeTeam = homeTeam;
        this.guestTeam = guestTeam;
    }

    public Match() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getMatchDate() {
        return matchDate;
    }

    public void setMatchDate(LocalDateTime matchDate) {
        this.matchDate = matchDate;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(Team homeTeam) {
        this.homeTeam = homeTeam;
    }

    public Team getGuestTeam() {
        return guestTeam;
    }

    public void setGuestTeam(Team awayTeam) {
        this.guestTeam = awayTeam;
    }

    public List<MatchPlayer> getPlayers() {
        return players;
    }

    public void setPlayers(List<MatchPlayer> players) {
        this.players = players;
    }
}
package com.tus.traunreut;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "matches")
public class Match {
    @Id
    @Column(name = "match_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Column(name = "home_goals")
    private Integer homeGoals;

    @Column(name = "guest_goals")
    private Integer guestGoals;

    @Column(name = "has_report")
    private boolean hasReport;

    @Column(name = "nuliga_match_id")
    private String nuligaMatchid;

    public Match(Long id, LocalDateTime matchDate, Team homeTeam, Team guestTeam, List<MatchPlayer> players, Integer homeGoals, Integer guestGoals, boolean hasReport, String nuligaMatchid) {
        this.id = id;
        this.matchDate = matchDate;
        this.homeTeam = homeTeam;
        this.guestTeam = guestTeam;
        this.players = players;
        this.homeGoals = homeGoals;
        this.guestGoals = guestGoals;
        this.hasReport = hasReport;
        this.nuligaMatchid = nuligaMatchid;
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

    public int getGuestGoals() {
        return guestGoals;
    }

    public void setGuestGoals(int guestGoals) {
        this.guestGoals = guestGoals;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    public void setHomeGoals(Integer homeGoals) {
        this.homeGoals = homeGoals;
    }

    public void setGuestGoals(Integer guestGoals) {
        this.guestGoals = guestGoals;
    }

    public boolean hasReport() {
        return hasReport;
    }

    public void setHasReport(boolean hasReport) {
        this.hasReport = hasReport;
    }

    public String getNuligaMatchId() {
        return nuligaMatchid;
    }

    public void setNuligaMatchId(String nuligaMatchid) {
        this.nuligaMatchid = nuligaMatchid;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(homeTeam, match.homeTeam) && Objects.equals(guestTeam, match.guestTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, guestTeam);
    }
}
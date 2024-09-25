package com.tus.traunreut.webserver.model;

import jakarta.persistence.*;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @Column(name = "team_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "club_id", referencedColumnName = "club_id")
    private Club club;

    @ManyToOne
    @JoinColumn(name = "gender_id", referencedColumnName = "gender_id")
    private Gender gender;

    @ManyToOne
    @JoinColumn(name = "league_id", referencedColumnName = "league_id")
    private League league;

    public Team(Long id, String name, Club club, Gender gender, League league) {
        this.id = id;
        this.name = name;
        this.club = club;
        this.gender = gender;
        this.league = league;
    }

    public Team() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Club getClub() {
        return club;
    }

    public void setClub(Club club) {
        this.club = club;
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
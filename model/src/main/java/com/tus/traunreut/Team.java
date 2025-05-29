package com.tus.traunreut;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "teams")
public class Team {

    @Id
    @Column(name = "team_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(name, team.name) && Objects.equals(club, team.club) && Objects.equals(gender, team.gender) && Objects.equals(league, team.league);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, club, gender, league);
    }
}
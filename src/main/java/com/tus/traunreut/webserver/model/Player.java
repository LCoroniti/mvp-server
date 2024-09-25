package com.tus.traunreut.webserver.model;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @Column(name = "player_id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "surname")
    private String surname;

    @ManyToOne
    @JoinColumn(name = "team_id", referencedColumnName = "team_id")
    private Team team;

    public Player(Long id, String firstName, String surname, Team team) {
        this.id = id;
        this.firstName = firstName;
        this.surname = surname;
        this.team = team;
    }

    public Player() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }
}
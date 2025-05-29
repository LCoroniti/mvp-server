package com.tus.traunreut.dto;


import com.tus.traunreut.MatchPlayer;

public class PlayerDto {
    private String id;
    private String firstName;
    private String surname;
    private String teamName;
    private int jerseyNumber;

    public PlayerDto(MatchPlayer player) {
        this.id = player.getPlayer().getId().toString();
        this.firstName = player.getPlayer().getFirstName();
        this.surname = player.getPlayer().getSurname();
        this.teamName = player.getPlayer().getTeam().getName();
        this.jerseyNumber = player.getJerseyNumber();
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }
}

package com.tus.traunreut.dto;


import com.tus.traunreut.MatchPlayer;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}

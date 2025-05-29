package com.tus.traunreut;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "matchplayers")
public class MatchPlayer {
    @EmbeddedId
    private MatchPlayerId id;

    @ManyToOne
    @MapsId("matchId")
    @JoinColumn(name = "match_id")
    @JsonIgnore
    private Match match;

    @ManyToOne
    @MapsId("playerId")
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(name = "jersey_number")
    private int jerseyNumber;

    public MatchPlayer(Match match, Player player, int jerseyNumber) {
        this.match = match;
        this.player = player;
        this.jerseyNumber = jerseyNumber;
        this.id = new MatchPlayerId(match.getId(), player.getId());
    }

    public MatchPlayer() {
    }

    public MatchPlayerId getId() {
        return id;
    }

    public void setId(MatchPlayerId id) {
        this.id = id;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MatchPlayer that = (MatchPlayer) o;
        return jerseyNumber == that.jerseyNumber && Objects.equals(match, that.match) && Objects.equals(player, that.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(match, player, jerseyNumber);
    }
}

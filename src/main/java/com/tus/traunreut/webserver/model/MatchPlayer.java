package com.tus.traunreut.webserver.model;

import jakarta.persistence.*;

@Entity
@Table(name = "matchplayers")
public class MatchPlayer {
    @EmbeddedId
    private MatchPlayerId id;

    @ManyToOne
    @MapsId("matchId")
    @JoinColumn(name = "match_id")
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
}

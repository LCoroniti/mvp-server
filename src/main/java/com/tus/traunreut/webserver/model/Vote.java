package com.tus.traunreut.webserver.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "votes")
public class Vote {

    @Id
    @Column(name = "vote_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "match_id", referencedColumnName = "match_id")
    private Match match;

    @ManyToOne
    @JoinColumn(name = "voted_player_id", referencedColumnName = "player_id")
    private Player player;

    @Column(name = "voter_id")
    private int voterId;

    public Vote(Long id, Match match, Player player, int voterId) {
        this.id = id;
        this.match = match;
        this.player = player;
        this.voterId = voterId;
    }

    public Vote(Match match, Player player, int voterId) {
        this.match = match;
        this.player = player;
        this.voterId = voterId;
    }

    public Vote() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public int getVoterId() {
        return voterId;
    }

    public void setVoterId(int voterId) {
        this.voterId = voterId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Vote vote = (Vote) o;
        return voterId == vote.voterId && Objects.equals(match, vote.match) && Objects.equals(player, vote.player);
    }

    @Override
    public int hashCode() {
        return Objects.hash(match, player, voterId);
    }
}
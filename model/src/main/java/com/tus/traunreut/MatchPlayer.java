package com.tus.traunreut;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "matchplayers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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

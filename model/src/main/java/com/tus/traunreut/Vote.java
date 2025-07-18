package com.tus.traunreut;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "votes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
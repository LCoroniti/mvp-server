package com.tus.traunreut;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Table(name = "teams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
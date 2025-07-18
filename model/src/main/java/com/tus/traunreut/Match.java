package com.tus.traunreut;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "matches")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Match {
    @Id
    @Column(name = "match_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_timestamp")
    private LocalDateTime matchDate;

    @ManyToOne
    @JoinColumn(name = "home_team_id", referencedColumnName = "team_id")
    private Team homeTeam;

    @ManyToOne
    @JoinColumn(name = "guest_team_id", referencedColumnName = "team_id")
    private Team guestTeam;

    @OneToMany(mappedBy = "match", fetch = FetchType.EAGER)
    private List<MatchPlayer> players;

    @Column(name = "home_goals")
    private Integer homeGoals;

    @Column(name = "guest_goals")
    private Integer guestGoals;

    @Column(name = "has_report")
    private boolean hasReport;

    @Column(name = "nuliga_match_id")
    private String nuligaMatchId;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Match match = (Match) o;
        return Objects.equals(homeTeam, match.homeTeam) && Objects.equals(guestTeam, match.guestTeam);
    }

    @Override
    public int hashCode() {
        return Objects.hash(homeTeam, guestTeam);
    }
}
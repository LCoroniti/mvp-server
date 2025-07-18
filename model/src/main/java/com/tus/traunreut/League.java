package com.tus.traunreut;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity
@Table(name = "leagues")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class League {

    @Id
    @Column(name = "league_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "match_plan_url")
    private String leaguePlanUrl;

    @Column(name = "group_id")
    private String groupdId;

    @Column
    private String season;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        League league = (League) o;
        return Objects.equals(name, league.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, leaguePlanUrl);
    }
}

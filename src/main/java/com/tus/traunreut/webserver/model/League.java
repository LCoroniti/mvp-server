package com.tus.traunreut.webserver.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "leagues")
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

    public League(Long id, String name, String leaguePlanUrl, String groupdId) {
        this.id = id;
        this.name = name;
        this.leaguePlanUrl = leaguePlanUrl;
        this.groupdId = groupdId;
    }

    public League() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeaguePlanUrl() {
        return leaguePlanUrl;
    }

    public void setLeaguePlanUrl(String leaguePlanUrl) {
        this.leaguePlanUrl = leaguePlanUrl;
    }

    public String getGroupdId() {
        return groupdId;
    }

    public void setGroupdId(String groupdId) {
        this.groupdId = groupdId;
    }

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

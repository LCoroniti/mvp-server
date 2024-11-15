package com.tus.traunreut.webserver.model;

import jakarta.persistence.*;

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

    public League(Long id, String name, String leaguePlanUrl) {
        this.id = id;
        this.name = name;
        this.leaguePlanUrl = leaguePlanUrl;
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
}

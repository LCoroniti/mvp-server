package com.tus.traunreut.webserver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "teams")
public class Team {
    @Id
    private String id;
    private String name;
    private String leagueId;

    public Team(String id, String name, String leagueId) {
        this.id = id;
        this.name = name;
        this.leagueId = leagueId;
    }

    public Team() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeagueId() {
        return leagueId;
    }

    public void setLeagueId(String leagueId)
    {
        this.leagueId = leagueId;
    }
}

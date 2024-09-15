package com.tus.traunreut.webserver.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "leagues")
public class League {
    @Id
    private String id;
    private String leagueName;
    private String gender;

    public League(String id, String leagueName, String gender) {
        this.id = id;
        this.leagueName = leagueName;
        this.gender = gender;
    }

    public League() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public void setLeagueName(String leagueName) {
        this.leagueName = leagueName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

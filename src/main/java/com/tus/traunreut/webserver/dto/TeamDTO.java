package com.tus.traunreut.webserver.dto;

public class TeamDTO {
    private String id;
    private String name;
    private ClubDTO club;

    // Constructors
    public TeamDTO(String id, String name, ClubDTO club) {
        this.id = id;
        this.name = name;
        this.club = club;
    }

    public TeamDTO() {}

    // Getters and Setters
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

    public ClubDTO getClub() {
        return club;
    }

    public void setClub(ClubDTO club) {
        this.club = club;
    }
}


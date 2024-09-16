package com.tus.traunreut.webserver.model;


import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;

public class Vote {
    private String playerId;
    @JsonIgnore
    private String ipAddress;

    public Vote(String playerId, String ipAddress) {
        this.playerId = playerId;
        this.ipAddress = ipAddress;
    }

    public Vote() {
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
}

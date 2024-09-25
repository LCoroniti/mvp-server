package com.tus.traunreut.webserver.model;

import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class MatchPlayerId implements Serializable {
    private Long matchId;
    private Long playerId;

    public MatchPlayerId(Long matchId, Long playerId) {
        this.matchId = matchId;
        this.playerId = playerId;
    }

    public MatchPlayerId() {
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }
}

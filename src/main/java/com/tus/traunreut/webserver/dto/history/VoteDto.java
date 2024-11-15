package com.tus.traunreut.webserver.dto.history;

import com.tus.traunreut.webserver.dto.PlayerDto;

public class VoteDto {
    private PlayerDto player;
    private double votingPercentage;

    public VoteDto(PlayerDto player, double votingPercentage) {
        this.player = player;
        this.votingPercentage = votingPercentage;
    }

    public VoteDto(PlayerDto player) {
        this.player = player;
    }

    public PlayerDto getPlayer() {
        return player;
    }

    public void setPlayer(PlayerDto player) {
        this.player = player;
    }

    public double getVotingPercentage() {
        return votingPercentage;
    }

    public void setVotingPercentage(double votingPercentage) {
        this.votingPercentage = votingPercentage;
    }
}

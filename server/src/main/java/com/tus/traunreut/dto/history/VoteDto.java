package com.tus.traunreut.dto.history;

import com.tus.traunreut.dto.PlayerDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}

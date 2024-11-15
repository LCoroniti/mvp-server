package com.tus.traunreut.webserver.dto.voting;

import com.tus.traunreut.webserver.dto.MatchDto;
import com.tus.traunreut.webserver.dto.PlayerDto;
import com.tus.traunreut.webserver.model.Match;

import java.util.ArrayList;
import java.util.List;

public class VotingMatchDto extends MatchDto {
    private List<PlayerDto> players;


    public VotingMatchDto(Match match, String homeTeamLogoBase64, String guestTeamLogoBase64) {
        super(match, homeTeamLogoBase64, guestTeamLogoBase64);
        this.players = (match.getPlayers() != null)
                ? match.getPlayers().stream().map(PlayerDto::new).toList()
                : new ArrayList<>();
    }

    public List<PlayerDto> getPlayers() {
        return players;
    }

    public void setPlayers(List<PlayerDto> players) {
        this.players = players;
    }
}

package com.tus.traunreut.webserver.dto.history;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tus.traunreut.webserver.dto.MatchDto;
import com.tus.traunreut.webserver.model.Match;

import java.util.List;

public class HistoryMatchDto extends MatchDto {
    private List<VoteDto> votes;
    @JsonIgnore
    private Match match;
    private int homeGoals;
    private int guestGoals;

    public HistoryMatchDto(Match match, String homeTeamLogoBase64, String guestTeamLogoBase64, List<VoteDto> votes) {
        super(match, homeTeamLogoBase64, guestTeamLogoBase64);
        this.votes = votes;
        this.match = match;
        this.homeGoals = match.getHomeGoals();
        this.guestGoals = match.getGuestGoals();
    }

    public HistoryMatchDto(Match match, List<VoteDto> votes) {
        this(match, null, null, votes);
    }

    public List<VoteDto> getVotes() {
        return votes;
    }

    public void setVotes(List<VoteDto> votes) {
        this.votes = votes;
    }

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    public int getGuestGoals() {
        return guestGoals;
    }

    public void setGuestGoals(int guestGoals) {
        this.guestGoals = guestGoals;
    }
}

package com.tus.traunreut.dto.history;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.tus.traunreut.Match;
import com.tus.traunreut.dto.MatchDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class HistoryMatchDto extends MatchDto {
    private List<VoteDto> votes;
    @JsonIgnore
    private Match match;
    private Integer homeGoals;
    private Integer guestGoals;

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
}

package com.tus.traunreut.dto;


import com.tus.traunreut.Gender;
import com.tus.traunreut.League;
import com.tus.traunreut.Match;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public abstract class MatchDto {
    private String id;
    private String homeTeamName;
    private String guestTeamName;
    private String homeTeamLogoBase64;
    private String guestTeamLogoBase64;
    private LocalDateTime matchDate;
    private Gender gender;
    private League league;

    public MatchDto(Match match, String homeTeamLogoBase64, String guestTeamLogoBase64) {
        this.id = match.getId().toString();
        this.homeTeamName = match.getHomeTeam().getName();
        this.guestTeamName = match.getGuestTeam().getName();
        this.homeTeamLogoBase64 = homeTeamLogoBase64;
        this.guestTeamLogoBase64 = guestTeamLogoBase64;
        this.matchDate = match.getMatchDate();
        this.gender = match.getHomeTeam().getGender();
        this.league = match.getHomeTeam().getLeague();
    }
}

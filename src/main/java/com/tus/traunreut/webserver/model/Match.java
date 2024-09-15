package com.tus.traunreut.webserver.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "matches")
public class Match {
    @Id
    private String id;
    private String homeTeam;
    private String awayTeam;
    private LocalDateTime matchDate;
    private List<Player> players;
    private List<Vote> votes;
    private String leagueId;
}

package com.tus.traunreut.webserver.service;

import com.tus.traunreut.webserver.model.Match;
import com.tus.traunreut.webserver.model.MatchPlayer;
import com.tus.traunreut.webserver.model.MatchPlayerId;
import com.tus.traunreut.webserver.model.Player;
import com.tus.traunreut.webserver.repository.MatchPlayerRepository;
import org.springframework.stereotype.Service;

@Service
public class MatchPlayerService {
    private final MatchPlayerRepository matchPlayerRepository;

    public MatchPlayerService(MatchPlayerRepository matchPlayerRepository) {
        this.matchPlayerRepository = matchPlayerRepository;
    }

    public MatchPlayer createMatchPlayer(Match match, Player player, int jerseyNumber) {
        MatchPlayer matchPlayer = new MatchPlayer();

        MatchPlayerId matchPlayerId = new MatchPlayerId();
        matchPlayerId.setMatchId(match.getId());
        matchPlayerId.setPlayerId(player.getId());
        matchPlayer.setId(matchPlayerId);

        matchPlayer.setMatch(match);
        matchPlayer.setPlayer(player);
        matchPlayer.setJerseyNumber(jerseyNumber);

        return matchPlayerRepository.save(matchPlayer);
    }

    public MatchPlayer saveMatchPlayer(MatchPlayer matchPlayer) {
        return matchPlayerRepository.save(matchPlayer);
    }
}

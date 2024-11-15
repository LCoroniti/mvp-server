package com.tus.traunreut.webserver.repository;

import com.tus.traunreut.webserver.model.Match;
import com.tus.traunreut.webserver.model.MatchPlayer;
import com.tus.traunreut.webserver.model.MatchPlayerId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MatchPlayerRepository extends JpaRepository<MatchPlayer, Long> {
    MatchPlayer findById(MatchPlayerId id);
    List<MatchPlayer> findByIdIn(List<MatchPlayerId> matchPlayerIds);
}

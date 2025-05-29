package com.tus.traunreut.repository;

import com.tus.traunreut.MatchPlayer;
import com.tus.traunreut.MatchPlayerId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MatchPlayerRepository extends JpaRepository<MatchPlayer, Long> {
    MatchPlayer findById(MatchPlayerId id);
    List<MatchPlayer> findByIdIn(List<MatchPlayerId> matchPlayerIds);
}

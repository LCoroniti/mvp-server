package com.tus.traunreut.webserver.repository;

import com.tus.traunreut.webserver.model.Vote;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    /**
     * Check if a vote exists for the given match and voter.
     */
    boolean existsByMatchIdAndVoterId(Long matchId, Integer voterId);

    @Query("SELECT v FROM Vote v WHERE v.match.id = :matchId AND v.voterId = :voterId")
    Optional<Vote> findVoteByMatchIdAndVoterId(@Param("matchId") Long matchId, @Param("voterId") int voterId);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO votes (match_id, voted_player_id, voter_id) VALUES (:matchId, :playerId, :voterId)", nativeQuery = true)
    void insertVote(Long matchId, Long playerId, int voterId);

    @Query("SELECT v.player.id FROM Vote v WHERE v.match.id = :matchId AND v.voterId = :voterId")
    Long findPlayerIdByMatchIdAndVoterId(@Param("matchId") Long matchId, @Param("voterId") int voterId);
}


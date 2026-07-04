package com.tus.traunreut.service;

import com.tus.traunreut.MatchPlayerId;
import com.tus.traunreut.Player;
import com.tus.traunreut.Vote;
import com.tus.traunreut.repository.MatchPlayerRepository;
import com.tus.traunreut.repository.PlayerRepository;
import com.tus.traunreut.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VotingService {
    private final VoteRepository voteRepository;
    private final PlayerRepository playerRepository;
    private final MatchPlayerRepository matchPlayerRepository;

    public VotingService(VoteRepository voteRepository, PlayerRepository playerRepository,
                         MatchPlayerRepository matchPlayerRepository) {
        this.voteRepository = voteRepository;
        this.playerRepository = playerRepository;
        this.matchPlayerRepository = matchPlayerRepository;
    }

    /**
     * Add a vote for the player. If the ip address has already voted for the player, false is returned.
     *
     * @throws IllegalArgumentException if the player is not part of the given match
     */
    public boolean vote(Long playerId, Long matchId, String ipAddress) {
        if (matchPlayerRepository.findById(new MatchPlayerId(matchId, playerId)) == null) {
            throw new IllegalArgumentException("Player " + playerId + " is not part of match " + matchId);
        }
        int hashedIpAddress = ipAddress.hashCode();
        if (voteRepository.existsByMatchIdAndVoterId(matchId, hashedIpAddress)) {
            return false;
        }
        voteRepository.insertVote(matchId, playerId, hashedIpAddress);
        return true;
    }

    public Optional<Player> getUserVote(Long matchId, String ipAddress) {
        int hashedIpAddress = ipAddress.hashCode();
        Long playerId = voteRepository.findPlayerIdByMatchIdAndVoterId(matchId, hashedIpAddress);
        if (playerId == null) {
            return Optional.empty();
        }
        return playerRepository.findById(playerId);
    }

    public void deleteVote(Long matchId, String ipAddress) {
        int hashedIpAddress = ipAddress.hashCode();
        Optional<Vote> vote = voteRepository.findVoteByMatchIdAndVoterId(matchId, hashedIpAddress);
        vote.ifPresent(voteRepository::delete);
    }
}

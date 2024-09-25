package com.tus.traunreut.webserver.service;

import com.tus.traunreut.webserver.model.Player;
import com.tus.traunreut.webserver.model.Vote;
import com.tus.traunreut.webserver.repository.PlayerRepository;
import com.tus.traunreut.webserver.repository.VoteRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VotingService {
    private final VoteRepository voteRepository;
    private final PlayerRepository playerRepository;

    public VotingService(VoteRepository voteRepository, PlayerRepository playerRepository) {
        this.voteRepository = voteRepository;
        this.playerRepository = playerRepository;
    }

    /**
     * Add a vote for the player. If the ip address has already voted for the player, false is returned.
     */
    public boolean vote(Long playerId, Long matchId, String ipAddress) {
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

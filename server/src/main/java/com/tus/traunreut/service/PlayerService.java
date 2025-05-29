package com.tus.traunreut.service;

import com.tus.traunreut.Player;
import com.tus.traunreut.Team;
import com.tus.traunreut.repository.PlayerRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;

    public PlayerService(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    /**
     * Create an entity of the player if it does not exist. Otherwise, return the player from the DB.
     */
    public Player createIfNotExist(String firstname, String lastname, Team team) {
        Optional<Player> player = playerRepository.findByFirstNameAndSurnameAndTeam(firstname, lastname, team);
        if (player.isPresent()) {
            return player.get();
        }
        Player p = new Player();
        p.setFirstName(firstname);
        p.setSurname(lastname);
        p.setTeam(team);
        return playerRepository.save(p);
    }
}

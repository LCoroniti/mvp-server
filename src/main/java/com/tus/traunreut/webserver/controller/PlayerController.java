package com.tus.traunreut.webserver.controller;

import com.tus.traunreut.webserver.model.Player;
import com.tus.traunreut.webserver.service.PlayerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("api/players")
public class PlayerController {
    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    @GetMapping
    public List<Player> getAllPlayers() {
        return playerService.getAllPlayers();
    }

    @PostMapping
    public Player createPlayer(@RequestBody Player player) {
        System.out.println("adding player: " + player.toString());
        return playerService.savePlayer(player);
    }
}

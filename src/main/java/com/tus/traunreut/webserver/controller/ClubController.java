package com.tus.traunreut.webserver.controller;

import com.tus.traunreut.webserver.model.Club;
import com.tus.traunreut.webserver.service.ClubService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clubs")
public class ClubController {
    private final ClubService clubService;

    public ClubController(ClubService clubService) {
        this.clubService = clubService;
    }

    @PostMapping
    public void saveClubs(@RequestBody List<Club> clubs)
    {
        clubService.saveClubs(clubs);
    }

    @GetMapping("/{id}")
    public Club getClubById(@PathVariable String id) {
        return clubService.getClubById(id);
    }
}

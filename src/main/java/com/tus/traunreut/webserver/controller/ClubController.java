package com.tus.traunreut.webserver.controller;

import com.tus.traunreut.webserver.model.Club;
import com.tus.traunreut.webserver.service.ClubService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}

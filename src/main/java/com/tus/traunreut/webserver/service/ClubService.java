package com.tus.traunreut.webserver.service;

import com.tus.traunreut.webserver.model.Club;
import com.tus.traunreut.webserver.repository.ClubRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClubService {
    private final ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public Club saveClub(Club club) {
        return clubRepository.save(club);
    }

    public List<Club> saveClubs(List<Club> clubs) {
        return clubRepository.saveAll(clubs);
    }

    public Club getClubById(String id) {
        return clubRepository.findById(id).orElseThrow(() -> new RuntimeException("Club not found"));
    }
}

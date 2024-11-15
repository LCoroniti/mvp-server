package com.tus.traunreut.webserver.repository;

import com.tus.traunreut.webserver.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
    Optional<Team> findByNameAndLeague_Name(String name, String leagueName);
}


package com.tus.traunreut.webserver.repository;

import com.tus.traunreut.webserver.model.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
    @Query("SELECT DISTINCT l.name FROM League l")
    List<String> findDistinctLeagueNames();

    Optional<League> findByName(String name);
}

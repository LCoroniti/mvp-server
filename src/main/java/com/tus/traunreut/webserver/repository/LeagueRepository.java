package com.tus.traunreut.webserver.repository;

import com.tus.traunreut.webserver.model.League;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeagueRepository extends JpaRepository<League, Long> {
}

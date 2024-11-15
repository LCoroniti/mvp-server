package com.tus.traunreut.webserver.repository;

import com.tus.traunreut.webserver.model.Player;
import com.tus.traunreut.webserver.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByFirstNameAndSurnameAndTeam(String firstName, String surname, Team team);
}


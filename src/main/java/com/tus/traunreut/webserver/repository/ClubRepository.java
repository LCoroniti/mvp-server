package com.tus.traunreut.webserver.repository;

import com.tus.traunreut.webserver.model.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClubRepository extends JpaRepository<Club, Long> {
}


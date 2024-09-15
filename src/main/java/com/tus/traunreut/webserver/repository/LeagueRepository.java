package com.tus.traunreut.webserver.repository;

import com.tus.traunreut.webserver.model.League;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LeagueRepository extends MongoRepository<League, String> {
}

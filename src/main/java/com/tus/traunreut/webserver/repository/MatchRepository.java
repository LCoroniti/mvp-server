package com.tus.traunreut.webserver.repository;

import com.tus.traunreut.webserver.model.Match;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MatchRepository extends MongoRepository<Match, String> {
    Match findFirstByOrderByMatchDateAsc();
}

package com.tus.traunreut.webserver.repository;

import com.tus.traunreut.webserver.model.Team;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TeamRepository extends MongoRepository<Team, String> {
}

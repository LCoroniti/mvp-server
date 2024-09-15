package com.tus.traunreut.webserver.repository;

import com.tus.traunreut.webserver.model.Club;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClubRepository extends MongoRepository<Club, String> {
    Club findByName(String name);
}

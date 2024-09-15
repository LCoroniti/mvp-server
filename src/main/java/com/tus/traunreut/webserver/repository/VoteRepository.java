package com.tus.traunreut.webserver.repository;

import com.tus.traunreut.webserver.model.Vote;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VoteRepository extends MongoRepository<Vote, String> {
}

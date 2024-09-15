package com.tus.traunreut.webserver.repository;

import com.tus.traunreut.webserver.model.Player;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PlayerRepository extends MongoRepository<Player, String> {
}

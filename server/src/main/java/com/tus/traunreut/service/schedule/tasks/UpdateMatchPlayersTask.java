
package com.tus.traunreut.service.schedule.tasks;

import com.tus.traunreut.MatchPlayer;
import com.tus.traunreut.ScheduledTask;
import com.tus.traunreut.scraper.MatchPlayerScraper;
import com.tus.traunreut.repository.PlayerRepository;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@DiscriminatorValue("2")
public class UpdateMatchPlayersTask extends ScheduledTask {
    public UpdateMatchPlayersTask(LocalDateTime executionTime) {
        super(executionTime);
    }
}
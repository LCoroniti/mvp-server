package com.tus.traunreut.service.schedule.tasks;

import com.tus.traunreut.*;
import com.tus.traunreut.repository.LeagueRepository;
import com.tus.traunreut.scraper.MatchIDScraper;
import com.tus.traunreut.service.MatchParsingService;
import com.tus.traunreut.repository.MatchRepository;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Entity
@DiscriminatorValue("1")
public class UpdateAllMatchesTask extends ScheduledTask {
    public UpdateAllMatchesTask(LocalDateTime executionTime) {
        super(executionTime);
    }
}

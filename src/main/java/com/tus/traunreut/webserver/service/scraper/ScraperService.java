package com.tus.traunreut.webserver.service.scraper;

import com.tus.traunreut.webserver.model.League;
import com.tus.traunreut.webserver.model.Match;
import com.tus.traunreut.webserver.model.MatchPlayer;
import com.tus.traunreut.webserver.repository.LeagueRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ScraperService {
    private final MatchScraper matchScraper;
    private final LeagueRepository leagueRepository;
    private final MatchPlayerScraper matchPlayerScraper;

    public ScraperService(MatchScraper matchScraper, LeagueRepository leagueRepository, MatchPlayerScraper matchPlayerScraper) {
        this.matchScraper = matchScraper;
        this.leagueRepository = leagueRepository;
        this.matchPlayerScraper = matchPlayerScraper;
    }

    public List<Match> scrapeMatches() throws IOException {
        List<Match> matches = new ArrayList<>();
        List<League> leagues = leagueRepository.findAll();
        leagues.forEach(league -> {
            try {
                matches.addAll(matchScraper.scrapeMatches(league));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return matches;
    }

    public Match scrapeMatch(Match match) throws IOException {
        List<Match> matches = matchScraper.scrapeMatches(match.getGuestTeam().getLeague());
        return matches.stream().filter(scraped -> scraped.getHomeTeam().equals(match.getHomeTeam()) &&
                scraped.getGuestTeam().equals(match.getGuestTeam())).findFirst().orElse(null);
    }

    public List<MatchPlayer> getMatchPlayers(Match match) {
        try {
            return matchPlayerScraper.getMatchPlayers(match);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

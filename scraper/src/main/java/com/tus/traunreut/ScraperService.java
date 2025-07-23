package com.tus.traunreut;

import com.tus.traunreut.scraper.MatchPlayerScraper;
import com.tus.traunreut.scraper.MatchScraper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ScraperService {
//    private final MatchScraper matchScraper;
//    private final MatchPlayerScraper matchPlayerScraper;

//    public ScraperService(MatchScraper matchScraper, MatchPlayerScraper matchPlayerScraper) {
//        this.matchScraper = Objects.requireNonNullElse(matchScraper, new MatchScraper());
//        this.matchPlayerScraper = Objects.requireNonNullElse(matchPlayerScraper, new MatchPlayerScraper());
//    }

//    public ScraperService()
//    {
//        this(null, null);
//    }
//
//    public List<Match> scrapeMatches(List<League> leagues) throws IOException {
//        List<Match> matches = new ArrayList<>();
//        leagues.forEach(league -> {
//            try {
//                matches.addAll(matchScraper.scrapeMatches(league));
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
//        });
//        return matches;
//    }
//
//    public Match scrapeMatch(Match match) throws IOException {
//        List<Match> matches = matchScraper.scrapeMatches(match.getGuestTeam().getLeague());
//        return matches.stream().filter(scraped -> scraped.getHomeTeam().equals(match.getHomeTeam()) &&
//                scraped.getGuestTeam().equals(match.getGuestTeam())).findFirst().orElse(null);
//    }
//
//    public void scrapeMatchId(League league, Match match) {
//        matchScraper.scrapeMeetingIds(league, match);
//    }
//
//    public List<MatchPlayer> getMatchPlayers(Match match) {
//        try {
//            return matchPlayerScraper.getMatchPlayers(match);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
}

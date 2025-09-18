package com.tus.traunreut.scraper;

import com.tus.traunreut.IScraper;
import com.tus.traunreut.League;
import com.tus.traunreut.Match;
import com.tus.traunreut.MatchPlayer;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScraperFactory {
    public IScraper<List<Element>> createMatchScraper(League league) {
        return new MatchScraper(league);
    }

    public IScraper<List<MatchPlayer>> createMatchPlayerScraper(Match match) {
        return new MatchPlayerScraper(match);
    }

    public IScraper<String> createMatchIdScraper(League league, Match match) {
        return new MatchIDScraper(league, match);
    }
}

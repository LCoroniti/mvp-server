package com.tus.traunreut;

import com.tus.traunreut.scraper.MatchPlayerScraper;
import com.tus.traunreut.scraper.MatchScraper;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScraperFactory {
    public IScraper<List<Element>> createMatchScraper(League league)
    {
        return new MatchScraper(league);
    }

    public IScraper<List<MatchPlayer>> createMatchPlayerScraper(Match match) {
        return new MatchPlayerScraper(match);
    }
}

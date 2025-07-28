package com.tus.traunreut.scraper;

import com.tus.traunreut.AbstractScraper;
import com.tus.traunreut.League;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.tus.traunreut.Log.NETWORK;
import static com.tus.traunreut.Log.NETWORK_LOG;

public class MatchScraper extends AbstractScraper<List<Element>> {
    private final League league;

    public MatchScraper(League league) {
        this.league = league;
    }

    /**
     * Fetch all matches for the league which is represented in the url.
     */
    @Override
    public List<Element> fetchData() throws DataFetchException {
        Connection connect = Jsoup.connect(league.getLeaguePlanUrl());
        NETWORK_LOG.info(NETWORK, "Fetching all matches for league {} from URL {}", league.getName(), league.getLeaguePlanUrl());
        Document doc;
        try {
            doc = connect.get();
        } catch (IOException e) {
            NETWORK_LOG.error(NETWORK, "Failed to establish connection for URL {}", league.getLeaguePlanUrl());
            throw new DataFetchException("Connection could not be established", e);
        }
        Element matchesTable = doc.select("table").last();
        if (matchesTable != null) {
            return matchesTable.select("tr").subList(1, matchesTable.select("tr").size()); // Skip header row
        }
        return new ArrayList<>();
    }
}

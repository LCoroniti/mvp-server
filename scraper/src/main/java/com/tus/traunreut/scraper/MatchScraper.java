package com.tus.traunreut.scraper;

import com.tus.traunreut.*;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MatchScraper extends AbstractScraper<List<Element>> {
    private static final Logger networkLogger = LoggerFactory.getLogger("NETWORK");
    private static final int DATE_INDEX = 1;
    private static final int TIME_INDEX = 2;
    private static final int HOME_TEAM_INDEX = 5;
    private static final int GUEST_TEAM_INDEX = 6;
    private static final int GOALS_INDEX = 7;
    private final League league;

    public MatchScraper (League league) {
        this.league = league;
    }
    /**
     * Fetch all matches for the league which is represented in the url.
     */
    @Override
    public List<Element> fetchData() throws DataFetchException {
        Connection connect = Jsoup.connect(league.getLeaguePlanUrl());
        networkLogger.info(Markers.NETWORK, "Fetching all matches for league {} from URL {}", league.getName(), league.getLeaguePlanUrl());
        Document doc;
        try {
            doc = connect.get();
        } catch (IOException e) {
            networkLogger.error("Failed to establish connection for URL {}", league.getLeaguePlanUrl());
            throw new DataFetchException("Connection could not be established", e);
        }
        Element matchesTable = doc.select("table").last();
        if (matchesTable != null) {
            return matchesTable.select("tr").subList(1, matchesTable.select("tr").size()); // Skip header row
        }
        return new ArrayList<>();
    }
}

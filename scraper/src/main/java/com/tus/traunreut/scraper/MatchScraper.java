package com.tus.traunreut.scraper;

import com.tus.traunreut.*;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

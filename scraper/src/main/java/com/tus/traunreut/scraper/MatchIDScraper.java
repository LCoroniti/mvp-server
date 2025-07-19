package com.tus.traunreut.scraper;

import com.tus.traunreut.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

public class MatchIDScraper extends AbstractScraper<String> {
    private static final Logger networkLogger = LoggerFactory.getLogger("NETWORK");
    private final League league;
    private final Match match;

    public MatchIDScraper(League league, Match match) {
        this.league = league;
        this.match = match;
    }
    /**
     * Scrape the match id for the given match from nuLiga.
     */
    @Override
    public String fetchData() {
        long now = DateTimeUtil.nowGermanSecondsRounded();
        String url = "https://hbde-live.liga.nu/nuScoreLiveRestBackend/api/1/meetings/" + league.getGroupdId() + "/time/" + now;
        networkLogger.info(Markers.NETWORK, "Scraping meetingId from Ticker: {} : {} (League = {})",
                match.getHomeTeam().getName(),
                match.getGuestTeam().getName(),
                match.getHomeTeam().getLeague());

        String meetingsJson = getRequest(url);

        JSONObject jsonObject = new JSONObject(meetingsJson);
        JSONArray meetings = jsonObject.getJSONArray("meetings");
        for (int i = 0; i < meetings.length(); i++) {
            JSONObject meeting = meetings.getJSONObject(i);

            String homeTeam = meeting.getString("teamHome");
            String guestTeam = meeting.getString("teamGuest");
            if (homeTeam.equals(match.getHomeTeam().getName()) && guestTeam.equals(match.getGuestTeam().getName())) {
                return meeting.getString("meetingID");
                //match.setNuligaMatchId(meeting.getString("meetingID"));
            }
        }
        networkLogger.warn(Markers.NETWORK, "No meetingId found for match: {} : {} (League = {})",
                match.getHomeTeam().getName(),
                match.getGuestTeam().getName(),
                match.getHomeTeam().getLeague());
        return null;
    }

}

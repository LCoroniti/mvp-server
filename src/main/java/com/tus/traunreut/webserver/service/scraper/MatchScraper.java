package com.tus.traunreut.webserver.service.scraper;

import com.tus.traunreut.webserver.log.Markers;
import com.tus.traunreut.webserver.model.League;
import com.tus.traunreut.webserver.model.Match;
import com.tus.traunreut.webserver.model.Team;
import com.tus.traunreut.webserver.service.TeamService;
import com.tus.traunreut.webserver.util.DateTimeUtil;
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

@Component
public class MatchScraper {
    private static final Logger networkLogger = LoggerFactory.getLogger("NETWORK");
    private static final int DATE_INDEX = 1;
    private static final int TIME_INDEX = 2;
    private static final int HOME_TEAM_INDEX = 5;
    private static final int GUEST_TEAM_INDEX = 6;
    private static final int GOALS_INDEX = 7;
    private final TeamService teamService;

    public MatchScraper(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Fetch all matches for the league which is represented in the url.
     */
    public List<Match> scrapeMatches(League league) throws IOException {
        Connection connect = Jsoup.connect(league.getLeaguePlanUrl());
        networkLogger.info(Markers.NETWORK, "Fetching all matches for league {} from URL {}", league.getName(), league.getLeaguePlanUrl());
        Document doc = connect.get();
        List<Match> matches = new ArrayList<>();
        Element matchesTable = doc.select("table").last();
        if (matchesTable != null) {
            Elements rows = matchesTable.select("tr");
            rows.removeFirst();
            for (Element row : rows) {
                Elements cells = row.select("td, th");
                String date = clearWhitespaces(cells.get(DATE_INDEX).text());
                String time = clearWhitespaces(cells.get(TIME_INDEX).text()).replaceAll("[^0-9:]", "");
                String homeTeam = cleanTeamName(cells.get(HOME_TEAM_INDEX).text());
                String guestTeam = cleanTeamName(cells.get(GUEST_TEAM_INDEX).text());
                Match match = new Match();
                LocalDateTime matchDate;
                try {
                    matchDate = parseLocalDateTime(date, time);
                } catch (DateTimeParseException e) {
                    continue;
                }
                match.setMatchDate(matchDate);
                Team home = teamService.getTeamByNameAndLeague(homeTeam, league.getName()).orElse(null);
                Team guest = teamService.getTeamByNameAndLeague(guestTeam, league.getName()).orElse(null);
                if (home == null || guest == null) {
                    continue;
                }
                match.setHomeTeam(home);
                match.setGuestTeam(guest);
                String goals = clearWhitespaces(cells.get(GOALS_INDEX).text());
                if (!goals.isBlank()) {
                    String[] parts = goals.split(":");
                    int homeGoals = Integer.parseInt(parts[0].trim());
                    int guestGoals = Integer.parseInt(parts[1].trim());
                    String meetingNbr = getMeetingNumber(cells.get(GOALS_INDEX).toString());
                    match.setNuligaMatchId(meetingNbr);
                    match.setHomeGoals(homeGoals);
                    match.setGuestGoals(guestGoals);
                    match.setHasReport(true);
                }
                matches.add(match);
            }
        }
        return matches;
    }

    /**
     * Scrape the meeting id for the given match
     */
    public void scrapeMeetingIds(League league, Match match) {
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
                match.setNuligaMatchId(meeting.getString("meetingID"));
            }
        }
    }

    private String getRequest(String url) {
        networkLogger.info(Markers.NETWORK, "GET request to {}", url);
        try (CloseableHttpClient client = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            request.setHeader("Accept", "application/json");

            return client.execute(request, response -> {
                int statusCode = response.getCode();
                if (statusCode >= 200 && statusCode < 300) {
                    return EntityUtils.toString(response.getEntity());
                } else {
                    throw new IOException("Unexpected response status: " + statusCode);
                }
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String getMeetingNumber(String htmlElement) {
        Pattern pattern = Pattern.compile("meeting=(\\d+)&amp");
        Matcher matcher = pattern.matcher(htmlElement);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }

    private String cleanTeamName(String name) {
        return name.replaceAll("\\s*\\([^)]*\\)", "").strip();
    }

    private String clearWhitespaces(String text) {
        return text.replaceAll("\\s", "");
    }

    private LocalDateTime parseLocalDateTime(String date, String time) throws DateTimeParseException {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.parse(time, timeFormatter);
        return LocalDateTime.of(localDate, localTime);
    }

}

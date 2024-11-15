package com.tus.traunreut.webserver.service.scraper;

import com.tus.traunreut.webserver.model.League;
import com.tus.traunreut.webserver.model.Match;
import com.tus.traunreut.webserver.model.Team;
import com.tus.traunreut.webserver.service.TeamService;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MatchScraper {
    private final int DATE_INDEX = 1;
    private final int TIME_INDEX = 2;
    private final int HOME_TEAM_INDEX = 5;
    private final int GUEST_TEAM_INDEX = 6;
    private final int GOALS_INDEX = 7;
    private final TeamService teamService;

    public MatchScraper(TeamService teamService) {
        this.teamService = teamService;
    }

    /**
     * Fetch all matches for the league which is represented in the url.
     */
    public List<Match> scrapeMatches(League league) throws IOException {
        Connection connect = Jsoup.connect(league.getLeaguePlanUrl());
        System.out.println("FETCHING NULIGA INFO FOR " + league.getName());
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
                match.setMatchDate(parseLocalDateTime(date, time));
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

    private LocalDateTime parseLocalDateTime(String date, String time) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate localDate = LocalDate.parse(date, dateFormatter);
        LocalTime localTime = LocalTime.parse(time, timeFormatter);
        LocalDateTime dateTime = LocalDateTime.of(localDate, localTime);
        return dateTime;
    }

}

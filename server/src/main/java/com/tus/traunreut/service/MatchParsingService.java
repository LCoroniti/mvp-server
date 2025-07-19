package com.tus.traunreut.service;

import com.tus.traunreut.League;
import com.tus.traunreut.Match;
import com.tus.traunreut.Team;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MatchParsingService {
    private static final int DATE_INDEX = 1;
    private static final int TIME_INDEX = 2;
    private static final int HOME_TEAM_INDEX = 5;
    private static final int GUEST_TEAM_INDEX = 6;
    private static final int GOALS_INDEX = 7;
    private TeamService teamService;

    public List<Match> parseTableData(List<Element> rows, League league) {
        List<Match> matches = new ArrayList<>();
        for (Element row : rows) {
            Elements cells = row.select("td, th");
            String date = clearWhitespaces(cells.get(DATE_INDEX).text());
            String time = clearWhitespaces(cells.get(TIME_INDEX).text()).replaceAll("[^0-9:]", "");
            String homeTeam = cleanTeamName(cells.get(HOME_TEAM_INDEX).text());
            String guestTeam = cleanTeamName(cells.get(GUEST_TEAM_INDEX).text());
            Match match = new Match();
            try {
                match.setMatchDate(parseLocalDateTime(date, time));
            } catch (DateTimeParseException e) {
                continue;
            }
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
        return matches;
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
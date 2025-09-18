package com.tus.traunreut.scraper;

import com.tus.traunreut.AbstractScraper;
import com.tus.traunreut.DateTimeUtil;
import com.tus.traunreut.League;
import com.tus.traunreut.Match;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import static com.tus.traunreut.Log.NETWORK;
import static com.tus.traunreut.Log.NETWORK_LOG;

@RequiredArgsConstructor(access = AccessLevel.MODULE)
public class MatchIDScraper extends AbstractScraper<String> {
    private final League league;
    private final Match match;

    /**
     * Scrape the match id for the given match from nuLiga. Return null if something went wrong.
     */
    @Override
    public String fetchData() throws DataFetchException {
        long now = DateTimeUtil.nowGermanSecondsRounded();
        String url = "https://hbde-live.liga.nu/nuScoreLiveRestBackend/api/1/meetings/" + league.getGroupdId() + "/time/" + now;
        NETWORK_LOG.info(NETWORK, "Scraping meetingId from Ticker: {} : {} (League = {})",
                match.getHomeTeam().getName(),
                match.getGuestTeam().getName(),
                match.getHomeTeam().getLeague());

        String meetingsJson;
        try {
            meetingsJson = getRequest(url);
        } catch (IOException e) {
            NETWORK_LOG.error(NETWORK, "Failed to fetch meetingId from Ticker for match with ID: {}", match.getId(), e);
            throw new DataFetchException("Failed to fetch meetingId from Ticker for match with ID: %d".formatted(match.getId()), e);
        }

        JSONObject jsonObject = new JSONObject(meetingsJson);
        JSONArray meetings = jsonObject.getJSONArray("meetings");
        for (int i = 0; i < meetings.length(); i++) {
            JSONObject meeting = meetings.getJSONObject(i);

            String homeTeam = meeting.getString("teamHome");
            String guestTeam = meeting.getString("teamGuest");
            if (homeTeam.equals(match.getHomeTeam().getName()) && guestTeam.equals(match.getGuestTeam().getName())) {
                return meeting.getString("meetingID");
            }
        }
        NETWORK_LOG.warn(NETWORK, "No meetingId found for match: {} : {} (League = {})",
                match.getHomeTeam().getName(),
                match.getGuestTeam().getName(),
                match.getHomeTeam().getLeague());
        return null;
    }
}

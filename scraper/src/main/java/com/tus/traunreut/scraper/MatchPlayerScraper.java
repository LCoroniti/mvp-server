package com.tus.traunreut.scraper;


import com.tus.traunreut.*;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MatchPlayerScraper extends AbstractScraper<List<MatchPlayer>> {
    private final Match match;

    public MatchPlayerScraper(Match match)
    {
        this.match = match;
    }

    private static final Logger networkLogger = LoggerFactory.getLogger("NETWORK");

    @Override
    public List<MatchPlayer> fetchData() {
        long now = DateTimeUtil.nowGermanSecondsRounded();
        String url = "https://hbde-live.liga.nu/nuScoreLiveRestBackend/api/1/players/" + match.getNuligaMatchId() + "/time/" + now;
        String playersJson = getRequest(url);
        JSONObject jsonObject = new JSONObject(playersJson);
        JSONArray matchPlayers = jsonObject.getJSONArray("meetingPersons");
        List<MatchPlayer> result = new ArrayList<>();
        for (int i = 0; i < matchPlayers.length(); i++) {
            JSONObject playerJson = matchPlayers.getJSONObject(i);

            String firstName = playerJson.getString("firstname");
            String lastName = playerJson.getString("lastname");
            int jerseyNbr = Integer.parseInt(playerJson.getString("nr"));
            Team team = playerJson.getBoolean("teamHome") ? match.getHomeTeam() : match.getGuestTeam();
            Player player = new Player();
            player.setFirstName(firstName);
            player.setSurname(lastName);
            player.setTeam(team);
            MatchPlayer matchPlayer = new MatchPlayer();
            matchPlayer.setMatch(match);
            matchPlayer.setPlayer(player);
            matchPlayer.setJerseyNumber(jerseyNbr);
            matchPlayer.setId(new MatchPlayerId(match.getId(), player.getId()));
            result.add(matchPlayer);
        }
        return result;
    }
}

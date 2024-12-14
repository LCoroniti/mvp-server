package com.tus.traunreut.webserver.service.scraper;


import com.tus.traunreut.webserver.model.*;
import com.tus.traunreut.webserver.service.PlayerService;
import com.tus.traunreut.webserver.util.DateTimeUtil;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class MatchPlayerScraper {
    private final PlayerService playerService;

    public MatchPlayerScraper(PlayerService playerService) {
        this.playerService = playerService;
    }

    public List<MatchPlayer>  getMatchPlayers(Match match) throws IOException {
        long now = DateTimeUtil.nowGermanSecondsRounded();
        String url = "https://hbde-live.liga.nu/nuScoreLiveRestBackend/api/1/players/" + match.getNuligaMatchId() + "/time/" + now;
        String playersJson = getRequest(url);
        System.out.println("REQUESTING NULIGA LIVE ...");

        JSONObject jsonObject = new JSONObject(playersJson);
        JSONArray matchPlayers = jsonObject.getJSONArray("meetingPersons");
        List<MatchPlayer> result = new ArrayList<>();
        for (int i = 0; i < matchPlayers.length(); i++) {
            JSONObject playerJson = matchPlayers.getJSONObject(i);

            String firstName = playerJson.getString("firstname");
            String lastName = playerJson.getString("lastname");
            int jerseyNbr = Integer.parseInt(playerJson.getString("nr"));
            Team team = playerJson.getBoolean("teamHome") ? match.getHomeTeam() : match.getGuestTeam();
            Player player = playerService.createIfNotExist(firstName, lastName, team);
            MatchPlayer matchPlayer = new MatchPlayer();
            matchPlayer.setMatch(match);
            matchPlayer.setPlayer(player);
            matchPlayer.setJerseyNumber(jerseyNbr);
            matchPlayer.setId(new MatchPlayerId(match.getId(), player.getId()));
            result.add(matchPlayer);
        }
        return result;
    }

    private String getRequest(String url) {
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
}

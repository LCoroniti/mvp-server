package com.tus.traunreut.scraper;


import com.tus.traunreut.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.tus.traunreut.Log.NETWORK;
import static com.tus.traunreut.Log.NETWORK_LOG;

@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class MatchPlayerScraper extends AbstractScraper<List<MatchPlayer>> {
    private final Match match;

    @Override
    public List<MatchPlayer> fetchData() throws DataFetchException {
        long now = DateTimeUtil.nowGermanSecondsRounded();
        String url = "https://hbde-live.liga.nu/nuScoreLiveRestBackend/api/1/players/" + match.getNuligaMatchId() + "/time/" + now;
        String playersJson;
        try {
            playersJson = getRequest(url);
        } catch (IOException e) {
            NETWORK_LOG.error(NETWORK, "Failed to fetch match player data for match with ID: {}", match.getId(), e);
            throw new DataFetchException("Failed to fetch match player data for match with ID: %d".formatted(match.getId()), e.getCause());
        }
        //TODO: move to MatchPlayerParsingService?
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
            result.add(matchPlayer);
        }
        return result;
    }
}

package ras.adlrr.RASBet.model.readers;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Participant;
import ras.adlrr.RASBet.model.APIGameReader;

// TODO: tratar de erros na loadGame
public class NFLOddsAPIReader implements APIGameReader{
    private JSONArray response;
    private int sport_id;
    
    public NFLOddsAPIReader(String response, int sport_id){
        this.response = new JSONArray(response);
        this.sport_id = sport_id;
    }

    public String getGameExternalId(JSONObject game){
        return (String) game.get("id");
    }

    public LocalDateTime getGameDate(JSONObject game) {
        String date = (String) game.get("commence_time");

        ZonedDateTime zdt = ZonedDateTime.parse(date);
        LocalDateTime dateTime = zdt.toLocalDateTime();
        return dateTime;
    }

    public float convertAmericanOdd(int price){
        float odd;
        if (price > 0)
            odd = (float) (1 + price / 100.0);
        else
            odd = (float) (1 + 100.0 / Math.abs(price));
        return (float) (Math.round(odd * 100.0) / 100.0);
    }

    public float getTeamOdd(JSONObject game, String team){
        float odd = 0;
        JSONArray bookmakers = (JSONArray) game.get("bookmakers");
        JSONObject fstBookmaker = (JSONObject) bookmakers.get(0);
        JSONArray markets = (JSONArray) fstBookmaker.get("markets");

        boolean foundH2H = false;
        for(int i = 0; i < markets.length() && !foundH2H; i++){
            JSONObject obj = (JSONObject) markets.get(i);
            String key = (String) obj.get("key");

            if (key.equals("h2h")){
                JSONArray outcomes = (JSONArray) obj.get("outcomes");
                for(int j = 0; j < outcomes.length(); j++){
                    JSONObject outcome = (JSONObject) outcomes.get(j);
                    String outcomeTeam = (String) outcome.get("name");
                    int price = (int) outcome.get("price");

                    if (outcomeTeam.equals(team))
                        odd = convertAmericanOdd(price);
                }
                foundH2H = true;
            }
        }

        return odd;
    }

    public Set<Participant> getGameParticipants(JSONObject game){
        Set<Participant> res = new HashSet<>();
        String homeTeam = (String) game.get("home_team");
        String awayTeam = (String) game.get("away_team");

        Participant home = new Participant(homeTeam, getTeamOdd(game, homeTeam), 0);
        Participant away = new Participant(awayTeam, getTeamOdd(game, awayTeam), 0);
        res.add(home); res.add(away);

        return res;
    }

    public int getGameState(JSONObject game){
        return Game.OPEN;
    }

    public int getSportID(){
        return this.sport_id;
    }

    public String makeName(JSONObject game){
        String homeTeam = (String) game.get("home_team");
        String awayTeam = (String) game.get("away_team");

        return awayTeam + " @ " + homeTeam;
    }

    @Override
    public List<Game> getAPIGames() {
        List<Game> res = new ArrayList<>();

        for(int i = 0; i < response.length() && i < 10; i++){
            JSONObject obj = (JSONObject) response.get(i);

            Game g = new Game(getGameExternalId(obj), getGameDate(obj), getGameState(obj), makeName(obj), getSportID(), getGameParticipants(obj));
            res.add(g);
        }

        return res;
    }
    
}

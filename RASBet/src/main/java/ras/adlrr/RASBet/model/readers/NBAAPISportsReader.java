package ras.adlrr.RASBet.model.readers;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import ras.adlrr.RASBet.model.APIGameReader;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Participant;

public class NBAAPISportsReader extends APIGameReader{
    private String sport_id;
    private int gamesToLoad;
    private String season;
    private String leagueID;

    public NBAAPISportsReader(String sport_id, int gamesToLoad){
        ReadJSONBehaviour readMethod = new ReadJSONFromExternalAPI();
        super.setReadMethod(readMethod);
        this.sport_id = sport_id;
        this.gamesToLoad = gamesToLoad;
        this.season = "2022-2023";
        this.leagueID = "12";
    }

    @Override
    public List<Game> getAPIGames() {
        String url = "https://v1.basketball.api-sports.io/games?league=" + this.leagueID + "&season=" + this.season;
        String response = super.readJSON(url, "jsons/nba.json", "b68a93e4291b512a0f3179eb9ee1bc2b");

        JSONArray games = (JSONArray) (new JSONObject(response).get("response"));
        List<Game> res = new ArrayList<>();

        int k = 0;
        for(int i = 0; i < games.length() && k < gamesToLoad; i++){
            JSONObject obj = (JSONObject) games.get(i);

            if (getGameState(obj) == Game.OPEN){
                Game g = new Game(getGameExternalId(obj), getGameDate(obj), getGameState(obj), getName(obj), getSportID(), getGameParticipants(obj));
                res.add(g);
                k++;
            }
        }
        
        return res;
    }

    public String getSportID() {
        return this.sport_id;
    }

    public String getGameExternalId(JSONObject game) {
        return game.get("id").toString();
    }

    public LocalDateTime getGameDate(JSONObject game) {
        String date = (String) game.get("date");
        ZonedDateTime zdt = ZonedDateTime.parse(date);
        LocalDateTime dateTime = zdt.toLocalDateTime();
        return dateTime;
    }

    public int getGameState(JSONObject game) {
        JSONObject status = (JSONObject) game.get("status");
        String status_short = (String) status.get("short");

        if (status_short.equals("NS") || status_short.equals("1H") || status_short.equals("HT")
                    || status_short.equals("2H") || status_short.equals("ET") || status_short.equals("P"))
            return Game.OPEN;
        else
            return Game.CLOSED;
    }

    public String getName(JSONObject game){
        JSONObject teams = (JSONObject) game.get("teams");
        JSONObject home = (JSONObject) teams.get("home");
        JSONObject away = (JSONObject) teams.get("away");
        String homeTeam = (String) home.get("name");
        String awayTeam = (String) away.get("name");

        return awayTeam + " @ " + homeTeam;
    }

    public List<Float> getOdds(JSONObject game){
        String url = "https://v1.basketball.api-sports.io/odds?league=" + this.leagueID + "&season=" + this.season + "&game=" + getGameExternalId(game);
        String path = "jsons/nba/odds_" + getGameExternalId(game) + ".json";
        String response = super.readJSON(url, path, "b68a93e4291b512a0f3179eb9ee1bc2b");
        List<Float> res = new ArrayList<>();
        res.add(0, 1.1f);
        res.add(1, 1.1f);

        JSONArray gameOdds = (JSONArray) (new JSONObject(response).get("response"));
        if(gameOdds.length() > 0){
            JSONObject gameOddsObj = (JSONObject) gameOdds.get(0);
            JSONArray bookmakers = (JSONArray) gameOddsObj.get("bookmakers");
            JSONObject fstBookmaker = (JSONObject) bookmakers.get(0);
            JSONArray bets = (JSONArray) fstBookmaker.get("bets");

            for(int i = 0; i < bets.length(); i++){
                JSONObject bmaker = (JSONObject) bets.get(i);
                String betName = (String) bmaker.get("name");
                if (betName.equals("Home/Away")){
                    JSONArray values = (JSONArray) bmaker.get("values");
                    for(int j = 0; j < 2 && j < values.length(); j++){
                        JSONObject valueObj = (JSONObject) values.get(j);
                        String value = (String) valueObj.get("value");
                        float odd = Float.parseFloat((String) valueObj.get("odd"));
                        if (value.equals("Home")){
                            res.add(0, odd);
                        }
                        else if (value.equals("Away")){
                            res.add(1, odd);
                        }
                    }
                }
            }
        }
        
        return res;
    }

    public Set<Participant> getGameParticipants(JSONObject game) {
        JSONObject teams = (JSONObject) game.get("teams");
        JSONObject home = (JSONObject) teams.get("home");
        JSONObject away = (JSONObject) teams.get("away");
        String homeTeam = (String) home.get("name");
        String awayTeam = (String) away.get("name");


        Set<Participant> ps = new HashSet<>();
        List<Float> odds = getOdds(game);
        Participant homeP = new Participant(homeTeam, odds.get(0), 0);
        Participant awayP = new Participant(awayTeam, odds.get(1), 0);

        ps.add(homeP); ps.add(awayP);

        return ps;
    }
}

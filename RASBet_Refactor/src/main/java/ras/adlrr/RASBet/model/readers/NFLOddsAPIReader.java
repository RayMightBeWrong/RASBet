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

public class NFLOddsAPIReader extends APIGameReader{
    private String sport_id;
    
    public NFLOddsAPIReader(String sport_id){
        this.sport_id = sport_id;
    }

    @Override
    public List<Game> getAPIGames() {
        String url = "https://api.the-odds-api.com/v4/sports/americanfootball_nfl/odds/?regions=us&oddsFormat=american&apiKey=70d50d68d47a79f93f43e9d7353e16ed";
        String sResponse = super.readJSON(url, "jsons/nfl.json", "b68a93e4291b512a0f3179eb9ee1bc2b");
        JSONArray response = new JSONArray(sResponse);
        List<Game> res = new ArrayList<>();

        for(int i = 0; i < response.length() && i < 10; i++){
            JSONObject obj = (JSONObject) response.get(i);

            Game g = new Game(getGameExternalId(obj), getGameDate(obj), getGameState(obj), makeName(obj), getSportID(), getGameParticipants(obj));
            res.add(g);
        }

        return res;
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

    public String getSportID(){
        return this.sport_id;
    }

    public String makeName(JSONObject game){
        String homeTeam = (String) game.get("home_team");
        String awayTeam = (String) game.get("away_team");

        return awayTeam + " @ " + homeTeam;
    }

    @Override
    public Set<Participant> getParticipantsUpdated(List<Game> games) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Set<Participant> updateScores(List<Game> games) {
        // TODO Auto-generated method stub
        return null;
    }

	@Override
	public List<Game> updateGamesState(List<Game> games) {
		// TODO Auto-generated method stub
		return null;
	}
}

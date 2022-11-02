package ras.adlrr.RASBet.model.readers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Participant;
import ras.adlrr.RASBet.model.APIGameReader;

public class NFLOddsAPIReader implements APIGameReader{
    private int sport_id;
    
    public NFLOddsAPIReader(int sport_id){
        this.sport_id = sport_id;
    }

    @Override
    public List<Game> getAPIGames() {
        JSONArray response = new JSONArray(readJSONfromHTTPRequest());
        //JSONArray response = new JSONArray(readFromLocalFile("nfl.json"));
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

    public int getSportID(){
        return this.sport_id;
    }

    public String makeName(JSONObject game){
        String homeTeam = (String) game.get("home_team");
        String awayTeam = (String) game.get("away_team");

        return awayTeam + " @ " + homeTeam;
    }

    public String readJSONfromHTTPRequest(){
        String url = "https://api.the-odds-api.com/v4/sports/americanfootball_nfl/odds/?regions=us&oddsFormat=american&apiKey=70d50d68d47a79f93f43e9d7353e16ed";
        HttpResponse<String> response = Unirest.get(url)
                                            .header("x-rapidapi-key", "b68a93e4291b512a0f3179eb9ee1bc2b")
                                            .header("x-rapidapi-host", "v3.football.api-sports.io").asString();
        try {
            Files.write( Paths.get("nfl.json"), response.getBody().getBytes());
        } catch (Exception e) {
            // ignore
        }

        return response.getBody();
    }

    public String readFromLocalFile(String path){
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader br;
            br = new BufferedReader(new FileReader(new File(path)));
            
            String st;
            while ((st = br.readLine()) != null)
                sb.append(st);
            }
        catch (Exception e){
            return "";
        }

        return sb.toString();
    }
}

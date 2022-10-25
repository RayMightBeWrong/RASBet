package ras.adlrr.RASBet.model.readers;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import kong.unirest.json.JSONArray;
import kong.unirest.json.JSONObject;
import ras.adlrr.RASBet.dao.GameRepository;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Participant;
import ras.adlrr.RASBet.model.APIGameReader;

// TODO: criar ID para o jogo, tratar de erros na loadGames e getID de NFL
public class NFLOddsAPIReader implements APIGameReader{
    private JSONArray response;
    private GameRepository gameRepository;
    
    public NFLOddsAPIReader(String response, GameRepository gameRepository){
        this.response = new JSONArray(response);
        this.gameRepository = gameRepository;
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

    public List<Participant> getGameParticipants(JSONObject game){
        List<Participant> res = new ArrayList<>();
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
        return 2;
    }

    @Override
    public int loadGames() {
        for(int i = 0; i < response.length() && i < 10; i++){
            JSONObject obj = (JSONObject) response.get(i);

            Game g = new Game(i, getGameExternalId(obj), getGameDate(obj), getGameParticipants(obj), getSportID(), getGameState(obj));
            //TODO - Ray
            //gameRepository.addGame(g);
        }
        return 0;
    }
    
}

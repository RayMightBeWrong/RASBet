package ras.adlrr.RASBet.model.readers;

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
import ras.adlrr.RASBet.dao.GameRepository;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Participant;
import ras.adlrr.RASBet.model.APIGameReader;

//TODO: erros + criar ID para a corrida + getID de F1
public class F1APISportsReader implements APIGameReader{
    private JSONArray response;
    private JSONObject nextRace;
    private GameRepository gameRepository;
    
    public F1APISportsReader(String response, GameRepository gameRepository){
        this.response = (JSONArray) (new JSONObject(response).get("response"));
        this.nextRace = (JSONObject) this.response.get(0);
        this.gameRepository = gameRepository;
    }
    
    public String getRaceExternalId(){
        return nextRace.get("id").toString();
    }

    public LocalDateTime getGameDate() {
        String date = (String) nextRace.get("date");

        ZonedDateTime zdt = ZonedDateTime.parse(date);
        LocalDateTime dateTime = zdt.toLocalDateTime();
        return dateTime;
    }

    public Set<Participant> getDrivers(){
        String url = "https://v1.formula-1.api-sports.io/rankings/races?race=" + getRaceExternalId();
        HttpResponse<String> response = Unirest.get(url)
                                            .header("x-rapidapi-key", "b68a93e4291b512a0f3179eb9ee1bc2b")
                                            .header("x-rapidapi-host", "v3.football.api-sports.io").asString();


        JSONArray driversResponse = (JSONArray) (new JSONObject(response.getBody()).get("response"));
        Set<Participant> res = new HashSet<>();
            
        for (int i = 0; i < driversResponse.length(); i++){
            JSONObject driverObj = (JSONObject) driversResponse.get(i);
            JSONObject driverAttributes = (JSONObject) driverObj.get("driver");

            Participant driver = new Participant((String) driverAttributes.get("name"), 0, i + 1);
            res.add(driver);
        }
          
        return res;
    }

    public int getSportID() {
        return 2;
    }

    public int getGameState(){
        return Game.OPEN;
    }

    @Override
    public int loadGames() {
        //Game g = new Game(1, getRaceExternalId(), getGameDate(), getGameState(), getSportID(), getDrivers());
        Game g = new Game(getRaceExternalId(), getGameDate(), getGameState(), null, getSportID(), null);
        // TODO - Ray
        //gameRepository.addGame(g);
        return 0;
    }
}

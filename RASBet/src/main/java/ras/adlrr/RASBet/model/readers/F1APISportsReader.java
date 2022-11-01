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
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Participant;
import ras.adlrr.RASBet.model.APIGameReader;

public class F1APISportsReader implements APIGameReader{
    private JSONArray response;
    private JSONObject nextRace;
    private int sport_id;
    
    public F1APISportsReader(String response, int sport_id){
        this.response = (JSONArray) (new JSONObject(response).get("response"));
        this.nextRace = (JSONObject) this.response.get(0);
        this.sport_id = sport_id;
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
        return this.sport_id;
    }

    public int getGameState(){
        return Game.OPEN;
    }

    @Override
    public List<Game> getAPIGames() {
        List<Game> res = new ArrayList<>();

        //Game g = new Game(getRaceExternalId(), getGameDate(), getGameState(), "F1", getSportID(), getDrivers());
        Game g = new Game(getRaceExternalId(), getGameDate(), getGameState(), "F1", getSportID(), null);
        res.add(g);

        return res;
    }
}

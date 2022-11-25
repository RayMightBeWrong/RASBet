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

public class F1APISportsReader extends APIGameReader{
    private String sport_id;
    private String season = "2022";
 
    public F1APISportsReader(String sport_id){
        ReadJSONBehaviour readMethod = new ReadJSONFromExternalAPI();
        super.setReadMethod(readMethod);
        this.sport_id = sport_id;
    }

    @Override
    public List<Game> getAPIGames() {
        String url = "https://v1.formula-1.api-sports.io/races?next=1&type=Race&season=" + season;
        String stringResponse = super.readJSON(url, "jsons/f1_nextrace.json", url);
        JSONArray response = (JSONArray) (new JSONObject(stringResponse).get("response"));
        
        if (response.length() > 0){
            JSONObject nextRace = (JSONObject) response.get(0);
            List<Game> res = new ArrayList<>();

            Game g = new Game(getRaceExternalId(nextRace), getGameDate(nextRace), getGameState(), getName(nextRace), getSportID(), getDrivers(nextRace));
            res.add(g);

            return res;
        }
        else
            return new ArrayList<>();
    }
    
    public String getRaceExternalId(JSONObject nextRace){
        return nextRace.get("id").toString();
    }

    public LocalDateTime getGameDate(JSONObject nextRace) {
        String date = (String) nextRace.get("date");

        ZonedDateTime zdt = ZonedDateTime.parse(date);
        LocalDateTime dateTime = zdt.toLocalDateTime();
        return dateTime;
    }

    public Set<Participant> getDrivers(JSONObject nextRace){
        String url = "https://v1.formula-1.api-sports.io/rankings/races?race=" + getRaceExternalId(nextRace);
        String stringResponse = super.readJSON(url, "jsons/f1_nextracegrid.json", "b68a93e4291b512a0f3179eb9ee1bc2b");
        JSONArray driversResponse = (JSONArray) (new JSONObject(stringResponse).get("response"));

        Set<Participant> res = new HashSet<>();
        
        if (driversResponse.length() > 0){
            for (int i = 0; i < driversResponse.length(); i++){
                JSONObject driverObj = (JSONObject) driversResponse.get(i);
                JSONObject driverAttributes = (JSONObject) driverObj.get("driver");

                Participant driver = new Participant((String) driverAttributes.get("name"), 0, i + 1);
                res.add(driver);
            }
        }
        else
            return getGrid();
          
        return res;
    }

    public Set<Participant> getGrid(){
        String url = "https://v1.formula-1.api-sports.io/rankings/drivers?season=" + season;
        String stringResponse = super.readJSON(url, "jsons/f1_grid.json", "b68a93e4291b512a0f3179eb9ee1bc2b");
        JSONArray response = (JSONArray) (new JSONObject(stringResponse).get("response"));

        Set<Participant> res = new HashSet<>();
        
        if(response.length() > 0){
            if (response.length() > 0){
                for (int i = 0; i < response.length() && i < 20; i++){
                    JSONObject driverObj = (JSONObject) response.get(i);
                    JSONObject driverAttributes = (JSONObject) driverObj.get("driver");

                    Participant driver = new Participant((String) driverAttributes.get("name"), 1.01f, i + 1);
                    res.add(driver);
                }
            }
        }
        else
            return null;
          
        return res;
    }

    public String getSportID() {
        return this.sport_id;
    }

    public int getGameState(){
        return Game.OPEN;
    }

    public String getName(JSONObject nextRace){
        JSONObject competition = (JSONObject) nextRace.getJSONObject("competition");
        return (String) competition.getString("name");
    }
}

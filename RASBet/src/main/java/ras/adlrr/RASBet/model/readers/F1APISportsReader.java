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

public class F1APISportsReader implements APIGameReader{
<<<<<<< HEAD
    private int sport_id;
    private String season = "2022";

    
    public F1APISportsReader(int sport_id){
        this.sport_id = sport_id;
    }

    @Override
    public List<Game> getAPIGames() {
        String url = "https://v1.formula-1.api-sports.io/races?next=1&type=Race&season=" + season;
        String stringResponse = readJSONfromHTTPRequest(url, "f1_nextrace.json");
        JSONArray response = (JSONArray) (new JSONObject(stringResponse).get("response"));
        JSONObject nextRace = (JSONObject) response.get(0);
        List<Game> res = new ArrayList<>();

        Game g = new Game(getRaceExternalId(nextRace), getGameDate(nextRace), getGameState(), getName(nextRace), getSportID(), getDrivers(nextRace));
        //Game g = new Game(getRaceExternalId(nextRace), getGameDate(nextRace), getGameState(), getName(nextRace), getSportID(), null);
        res.add(g);

        return res;
=======
    private JSONArray response;
    private JSONObject nextRace;
    private int sport_id;
    
    public F1APISportsReader(String response, int sport_id){
        this.response = (JSONArray) (new JSONObject(response).get("response"));
        this.nextRace = (JSONObject) this.response.get(0);
        this.sport_id = sport_id;
>>>>>>> a14d69bd746ac6a3556ecb31baad3dc33fb60630
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
        String stringResponse = readJSONfromHTTPRequest(url, "f1_nextracegrid.json");
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
        String stringResponse = readJSONfromHTTPRequest(url, "f1_grid.json");
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

    public int getSportID() {
        return this.sport_id;
    }

    public int getGameState(){
        return Game.OPEN;
    }

<<<<<<< HEAD
    public String getName(JSONObject nextRace){
        JSONObject competition = (JSONObject) nextRace.getJSONObject("competition");
        return (String) competition.getString("name");
    }

    public String readJSONfromHTTPRequest(String url, String path){
        HttpResponse<String> response = Unirest.get(url)
                                            .header("x-rapidapi-key", "b68a93e4291b512a0f3179eb9ee1bc2b")
                                            .header("x-rapidapi-host", "v3.football.api-sports.io").asString();
        try {
            Files.write( Paths.get(path), response.getBody().getBytes());
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
=======
    @Override
    public List<Game> getAPIGames() {
        List<Game> res = new ArrayList<>();

        //Game g = new Game(getRaceExternalId(), getGameDate(), getGameState(), "F1", getSportID(), getDrivers());
        Game g = new Game(getRaceExternalId(), getGameDate(), getGameState(), "F1", getSportID(), null);
        res.add(g);

        return res;
>>>>>>> a14d69bd746ac6a3556ecb31baad3dc33fb60630
    }
}

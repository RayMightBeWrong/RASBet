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
    private int racesToLoad = 3;
    private String response;
 
    public F1APISportsReader(String sport_id){
        ReadJSONBehaviour readMethod = new ReadJSONFromExternalAPI();
        super.setReadMethod(readMethod);
        this.sport_id = sport_id;
    }

    @Override
    public List<Game> getAPIGames() {
        String url = "https://v1.formula-1.api-sports.io/races?type=Race&season=" + season;
        this.response = super.readJSON(url, "jsons/f1.json", "b68a93e4291b512a0f3179eb9ee1bc2b");
        
        /*response = "";
        try {
            response = super.readFromLocalFile("jsons/f1.json");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        JSONArray races = (JSONArray) (new JSONObject(this.response).get("response"));
        List<Game> res = new ArrayList<>();
        
        if (races.length() > 0){
            int added = 0;
            for(int i = 0; i < races.length() && added < racesToLoad; i++){
                JSONObject obj = (JSONObject) races.get(i);
                int status = getGameState(obj);

                if (status == Game.OPEN && added < this.racesToLoad){
                    String extID = getGameExternalId(obj);
                    Game g = new Game(extID, getGameDate(obj), status, getName(obj), getSportID(), getDrivers(extID));
                    res.add(g);
                    added++;
                }
            }

            return res;
        }
        else
            return new ArrayList<>();
    }
    
    public String getGameExternalId(JSONObject race){
        return race.get("id").toString();
    }

    public LocalDateTime getGameDate(JSONObject nextRace) {
        String date = (String) nextRace.get("date");
        ZonedDateTime zdt = ZonedDateTime.parse(date);
        LocalDateTime dateTime = zdt.toLocalDateTime();

        return dateTime;
    }

    public Set<Participant> getDrivers(String extID){
        String url = "https://v1.formula-1.api-sports.io/rankings/races?race=" + extID;
        String stringResponse = super.readJSON(url, "jsons/f1/race_" + extID + ".json", "b68a93e4291b512a0f3179eb9ee1bc2b");

        /*String stringResponse = "";
        try {
            stringResponse = super.readFromLocalFile("jsons/f1/race_" + extID + ".json");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        JSONArray driversResponse = (JSONArray) (new JSONObject(stringResponse).get("response"));
        Set<Participant> res = new HashSet<>();
        
        if (driversResponse.length() > 0){
            for (int i = 0; i < driversResponse.length(); i++){
                JSONObject driverObj = (JSONObject) driversResponse.get(i);
                JSONObject driverAttributes = (JSONObject) driverObj.get("driver");
                int position = (int) driverObj.get("position");

                System.out.println(driverAttributes.get("name") + ": " + position);

                Participant driver = new Participant((String) driverAttributes.get("name"), 1.0f, position);
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

                    Participant driver = new Participant((String) driverAttributes.get("name"), 1.0f, i + 1);
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

    public int getGameState(JSONObject race){
        String status = (String) race.get("status");

        if (status.equals("Scheduled") || status.equals("Postponed") || status.equals("Live"))
            return Game.OPEN;
        else
            return Game.CLOSED;
    }

    public String getName(JSONObject race){
        JSONObject competition = (JSONObject) race.getJSONObject("competition");
        return (String) competition.getString("name");
    }

    @Override
    public Set<Participant> updateOdds(List<Game> games) {
        return new HashSet<>();
    }

    public void updateParticipantsScores(Set<Participant> participants, String game) {
        Set<Participant> drivers = getDrivers(game);

        for(Participant p1: participants){
                for(Participant p2: drivers){
                if (p1.getName().equals(p2.getName())){
                    p1.setScore(p2.getScore());
                }
            }
        }
    }

    @Override
    public Set<Participant> updateScores(List<Game> games) {
        Set<Participant> res = new HashSet<>();

        for(Game g: games){
            Set<Participant> ps = g.getParticipants();
            updateParticipantsScores(ps, g.getExtID());

            for(Participant p: ps)
                res.add(p);
        }

        return res;
    }

    @Override
    public List<Game> updateGamesState(List<Game> games) {
        List<Game> res = new ArrayList<>();
        JSONArray gamesArray = (JSONArray) (new JSONObject(response).get("response"));
        
        for(int i = 0; i < gamesArray.length() ; i++){
            JSONObject obj = (JSONObject) gamesArray.get(i);

            for (int j = 0; j < games.size(); j++){
                if (getGameExternalId(obj).equals(games.get(j).getExtID())){
                    Game g = games.get(j);
                    
                    if (g.getState() != getGameState(obj)){
                        g.setState(getGameState(obj));
                        res.add(g);
                    }

                    break;
                }
            }
        }

        return res;
    }
}

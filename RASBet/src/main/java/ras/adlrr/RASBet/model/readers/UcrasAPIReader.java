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

public class UcrasAPIReader extends APIGameReader{
    private String sport_id;

    public UcrasAPIReader(String sport_id){
        ReadJSONBehaviour readMethod = new ReadJSONBasic();
        super.setReadMethod(readMethod);
        this.sport_id = sport_id;
    }

    public List<Game> getAPIGames(){
        String json = super.readJSON("http://ucras.di.uminho.pt/v1/games/", null, null);

        if (json.equals(""))
            return null;
            
        List<Game> res = new ArrayList<>();

        JSONArray ja = new JSONArray(json);
        for(int i = 0; i < ja.length(); i++){
            JSONObject jo = (JSONObject) ja.get(i);
            Game g = new Game(getGameExternalId(jo), getGameDate(jo), getGameState(), makeName(jo), getSportID(), getGameParticipants(jo));
            res.add(g);
        }

        return res;
    }

    public String getGameExternalId(JSONObject jo){
        return (String) jo.get("id");
    }

    public LocalDateTime getGameDate(JSONObject jo) {
        String iso8601 = (String) jo.get("commenceTime");
        ZonedDateTime zdt = ZonedDateTime.parse(iso8601);
        LocalDateTime ldt = zdt.toLocalDateTime();

        return ldt;
    }

    public Set<Participant> getGameParticipants(JSONObject jo){
        String homeTeam = (String) jo.get("homeTeam");
        String awayTeam = (String) jo.get("awayTeam");

        JSONArray bookmakers = (JSONArray) jo.get("bookmakers");
        JSONObject fst = (JSONObject) bookmakers.get(0);
        JSONArray markets = (JSONArray) fst.get("markets");
        JSONObject markets2 = (JSONObject) markets.get(0);
        JSONArray outcomes = (JSONArray) markets2.get("outcomes");

        float drawOdd = 0, homeOdd = 0, awayOdd = 0;
        for (int j = 0; j < outcomes.length(); j++){
            JSONObject obj = (JSONObject) outcomes.get(j);
            Number odd = (Number) obj.get("price");
            if (homeTeam.equals(obj.get("name"))){
                homeOdd = odd.floatValue();
            }
            else if (awayTeam.equals(obj.get("name"))){
                awayOdd = odd.floatValue();
            }
            else if (obj.get("name").equals("Draw")){
                drawOdd = odd.floatValue();
            }
        }
        Participant home = new Participant(homeTeam, homeOdd, 0);
        Participant away = new Participant(awayTeam, awayOdd, 0);
        Participant draw = new Participant("draw", drawOdd, 0);


        Set<Participant> ps = new HashSet<>();
        ps.add(home); ps.add(away); ps.add(draw);

        return ps;
    }

    public int getGameState(){
        return Game.OPEN;
    }

    public String getSportID(){
        return this.sport_id;
    }

    public String makeName(JSONObject jo){
        String homeTeam = (String) jo.get("homeTeam");
        String awayTeam = (String) jo.get("awayTeam");

        return homeTeam + " vs " + awayTeam;
    }
}

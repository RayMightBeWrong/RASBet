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
    private String response;

    public UcrasAPIReader(String sport_id){
        ReadJSONBehaviour readMethod = new ReadJSONBasic();
        super.setReadMethod(readMethod);
        this.sport_id = sport_id;
    }

    public List<Game> getAPIGames(){
        String json = super.readJSON("http://ucras.di.uminho.pt/v1/games/", "jsons/ucras.json", null);
        /*String json = "";
        try {
            json = super.readFromLocalFile("jsons/ucras.json");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        response = json;

        if (json.equals(""))
            return null;
            
        List<Game> res = new ArrayList<>();

        JSONArray ja = new JSONArray(json);
        for(int i = 0; i < ja.length(); i++){
            JSONObject jo = (JSONObject) ja.get(i);
            Game g = new Game(getGameExternalId(jo), getGameDate(jo), getGameState(jo), makeName(jo), getSportID(), getGameParticipants(jo));
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

        float drawOdd = 1.0f, homeOdd = 1.0f, awayOdd = 1.0f;
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

    public int getGameState(JSONObject jo){
        Boolean completed = (Boolean) jo.get("completed");
        
        if (completed)
            return Game.CLOSED;
        else
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

    @Override
    public Set<Participant> updateOdds(List<Game> games) {
        return new HashSet<>();
    }

    @Override
    public Set<Participant> updateScores(List<Game> games) {
        games.removeIf(g -> g.getExtID().length() < 8);
        Set<Participant> res = new HashSet<>();
        JSONArray gamesArray = (JSONArray) new JSONArray(response);

        for(int i = 0; i < gamesArray.length() ; i++){
            JSONObject obj = (JSONObject) gamesArray.get(i);

            for (int j = 0; j < games.size(); j++){
                if (getGameExternalId(obj).equals(games.get(j).getExtID())){
                    Game g = games.get(j);
                    Set<Participant> ps = g.getParticipants();
                    String home = extractTeamFromGameName(g.getTitle(), true);
                    String away = extractTeamFromGameName(g.getTitle(), false);

                    String score = (String) obj.get("scores");

                    if (score != null){
                        for(Participant p: ps){
                            if (p.getName().equals(home)){
                                int goals = extractScore(score, true);
                                p.setScore(goals);
                            }
                            else if (p.getName().equals(away)){
                                int goals = extractScore(score, false);
                                p.setScore(goals);
                            }
                            res.add(p);
                        }
                    }

                    break;
                }
            }
        }

        return res;
    }


    @Override
    public List<Game> updateGamesState(List<Game> games) {
        games.removeIf(g -> g.getExtID().length() < 8);
        List<Game> res = new ArrayList<>();
        JSONArray gamesArray = (JSONArray) new JSONArray(response);

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

    private String extractTeamFromGameName(String name, boolean home){
        String[] teams = name.split(" vs ");
        if (home)
            return teams[0];
        else
            return teams[1];
    }

    private int extractScore(String score, boolean home){
        String[] scores = score.split("x");
        if (home)
            return Integer.parseInt(scores[0]);
        else
            return Integer.parseInt(scores[1]);
    }

}

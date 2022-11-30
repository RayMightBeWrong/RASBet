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

public class NFLAPISportsReader extends APIGameReader{
    private String sport_id;
    private String response;
    private int gamesToLoad = 2;
    
    public NFLAPISportsReader(String sport_id){
        ReadJSONBehaviour readMethod = new ReadJSONFromExternalAPI();
        super.setReadMethod(readMethod);
        this.sport_id = sport_id;
    }

    @Override
    public List<Game> getAPIGames() {
        String url = "https://v1.american-football.api-sports.io/games?league=1&season=2022"; 
        response = super.readJSON(url, "jsons/nfl.json", "b68a93e4291b512a0f3179eb9ee1bc2b");
        
        /*response = "";
        try {
            response = super.readFromLocalFile("jsons/nfl.json");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        JSONArray games = (JSONArray) (new JSONObject(response).get("response"));
        List<Game> res = new ArrayList<>();
        
        int added = 0;
        for(int i = 0; i < games.length() && added < gamesToLoad; i++){
            JSONObject obj = (JSONObject) games.get(i);
            int status = getGameState(obj);

            if(status == Game.OPEN){
                Game g = new Game(getGameExternalId(obj), getGameDate(obj), status, makeName(obj), getSportID(), getGameParticipants(obj));
                res.add(g);
                added ++;
            }
        }

        return res;
    }

    public String getGameExternalId(JSONObject obj){
        JSONObject objGame = (JSONObject) obj.get("game");
        return objGame.get("id").toString();
    }

    public LocalDateTime getGameDate(JSONObject obj) {
        JSONObject game = (JSONObject) obj.get("game");
        JSONObject date = (JSONObject) game.get("date");

        String dateS = (String) date.get("date");
        String time = (String) date.get("time");

        ZonedDateTime zdt = ZonedDateTime.parse(dateS + "T" + time + "+00:00");
        LocalDateTime dateTime = zdt.toLocalDateTime();

        return dateTime;
    }

    public Set<Participant> getGameParticipants(JSONObject obj){
        JSONObject teams = (JSONObject) obj.get("teams");
        JSONObject home = (JSONObject) teams.get("home");
        JSONObject away = (JSONObject) teams.get("away");
        String homeTeam = (String) home.get("name");
        String awayTeam = (String) away.get("name");

        Set<Participant> ps = new HashSet<>();
        Participant homeP = new Participant(homeTeam, 1.0f, 0);
        Participant drawP = new Participant("draw", 1.0f, 0);
        Participant awayP = new Participant(awayTeam, 1.0f, 0);
        ps.add(homeP); ps.add(drawP); ps.add(awayP);

        return ps;
    }

    public int getGameState(JSONObject obj){
        JSONObject game = (JSONObject) obj.get("game");
        JSONObject status = (JSONObject) game.get("status");
        String status_short = (String) status.get("short");

        if (status_short.equals("NS") || status_short.equals("Q1") || status_short.equals("Q2")
                    || status_short.equals("Q3") || status_short.equals("Q4") || status_short.equals("OT") 
                    || status_short.equals("HT") || status_short.equals("PST"))
            return Game.OPEN;
        else
            return Game.CLOSED;
    }

    public String getSportID(){
        return this.sport_id;
    }

    public String makeName(JSONObject game){
        JSONObject teams = (JSONObject) game.get("teams");
        JSONObject home = (JSONObject) teams.get("home");
        JSONObject away = (JSONObject) teams.get("away");
        String homeTeam = (String) home.get("name");
        String awayTeam = (String) away.get("name");

        return awayTeam + " @ " + homeTeam;
    }

    @Override
    public Set<Participant> updateOdds(List<Game> games) {
        return new HashSet<>();
    }

    @Override
    public Set<Participant> updateScores(List<Game> games) {
        Set<Participant> res = new HashSet<>();
        JSONArray gamesArray = (JSONArray) (new JSONObject(response).get("response"));

        for(int i = 0; i < gamesArray.length() ; i++){
            JSONObject obj = (JSONObject) gamesArray.get(i);

            for (int j = 0; j < games.size(); j++){
                if (getGameExternalId(obj).equals(games.get(j).getExtID())){
                    Game g = games.get(j);
                    Set<Participant> ps = g.getParticipants();
                    String home = extractTeamFromGameName(g.getTitle(), true);
                    String away = extractTeamFromGameName(g.getTitle(), false);

                    JSONObject scores = (JSONObject) obj.get("scores");
                    JSONObject home_score = (JSONObject) scores.get("home");
                    JSONObject away_score = (JSONObject) scores.get("away");

                    if (home_score.get("total") != null){
                        for(Participant p: ps){
                            if (p.getName().equals(home)){
                                int total_home = (int) home_score.get("total");
                                p.setScore(total_home);
                            }
                            else if (p.getName().equals(away)){
                                int total_away = (int) away_score.get("total");
                                p.setScore(total_away);
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

    private String extractTeamFromGameName(String name, boolean home){
        String[] teams = name.split(" @ ");
        if (home)
            return teams[1];
        else
            return teams[0];
    }
}

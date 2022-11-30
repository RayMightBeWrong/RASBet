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

public class NBAAPISportsReader extends APIGameReader{
    private String sport_id;
    private int gamesToLoad = 10;
    private String season;
    private String leagueID;
    private String response;

    public NBAAPISportsReader(String sport_id){
        ReadJSONBehaviour readMethod = new ReadJSONFromExternalAPI();
        super.setReadMethod(readMethod);
        this.sport_id = sport_id;
        this.season = "2022-2023";
        this.leagueID = "12";
    }

    @Override
    public List<Game> getAPIGames() {
        String url = "https://v1.basketball.api-sports.io/games?league=" + this.leagueID + "&season=" + this.season;
        response = super.readJSON(url, "jsons/nba.json", "b68a93e4291b512a0f3179eb9ee1bc2b");

        /*
        response = "";
        try {
            response = super.readFromLocalFile("jsons/nba.json");
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        JSONArray games = (JSONArray) (new JSONObject(response).get("response"));
        List<Game> res = new ArrayList<>();

        int added = 0;
        for(int i = 0; i < games.length() && added < gamesToLoad; i++){
            JSONObject obj = (JSONObject) games.get(i);

            if (getGameState(obj) == Game.OPEN){
                Game g = new Game(getGameExternalId(obj), getGameDate(obj), getGameState(obj), getName(obj), getSportID(), getGameParticipants(obj, leagueID));
                res.add(g);
                added++;
            }
        }
        
        return res;
    }

    public String getSportID() {
        return this.sport_id;
    }

    public String getGameExternalId(JSONObject game) {
        return game.get("id").toString();
    }

    public LocalDateTime getGameDate(JSONObject game) {
        String date = (String) game.get("date");
        ZonedDateTime zdt = ZonedDateTime.parse(date);
        LocalDateTime dateTime = zdt.toLocalDateTime();
        return dateTime;
    }

    public int getGameState(JSONObject game) {
        JSONObject status = (JSONObject) game.get("status");
        String status_short = (String) status.get("short");

        if (status_short.equals("NS") || status_short.equals("POST") || status_short.equals("Q1") || status_short.equals("Q2")
            || status_short.equals("Q3") || status_short.equals("Q4") || status_short.equals("OT") || status_short.equals("BT")
            || status_short.equals("HT"))
            return Game.OPEN;
        else
            return Game.CLOSED;
    }

    public String getName(JSONObject game){
        JSONObject teams = (JSONObject) game.get("teams");
        JSONObject home = (JSONObject) teams.get("home");
        JSONObject away = (JSONObject) teams.get("away");
        String homeTeam = (String) home.get("name");
        String awayTeam = (String) away.get("name");

        return awayTeam + " @ " + homeTeam;
    }

    public List<Float> getOdds(String extID, String idLeague){
        String url = "https://v1.basketball.api-sports.io/odds?league=" + this.leagueID + "&season=" + this.season + "&game=" + extID;
        String path = "jsons/nba/odds_" + extID + ".json";
        String response = super.readJSON(url, path, "b68a93e4291b512a0f3179eb9ee1bc2b");

        /*
        String response = "";
        try {
            response = super.readFromLocalFile("jsons/nba/odds_" + extID + ".json");
        } catch (Exception e) {
            e.printStackTrace();
        }*/


        List<Float> res = new ArrayList<>();
        res.add(0, 1.0f); res.add(1, 1.0f);

        JSONArray gameOdds = (JSONArray) (new JSONObject(response).get("response"));
        if(gameOdds.length() > 0){
            JSONObject gameOddsObj = (JSONObject) gameOdds.get(0);
            JSONArray bookmakers = (JSONArray) gameOddsObj.get("bookmakers");
            JSONObject fstBookmaker = (JSONObject) bookmakers.get(0);
            JSONArray bets = (JSONArray) fstBookmaker.get("bets");

            for(int i = 0; i < bets.length(); i++){
                JSONObject bmaker = (JSONObject) bets.get(i);
                String betName = (String) bmaker.get("name");

                if (betName.equals("Home/Away")){
                    JSONArray values = (JSONArray) bmaker.get("values");

                    for(int j = 0; j < 2 && j < values.length(); j++){
                        JSONObject valueObj = (JSONObject) values.get(j);
                        String value = (String) valueObj.get("value");
                        float odd = Float.parseFloat((String) valueObj.get("odd"));

                        if (value.equals("Home")){
                            res.set(0, odd);
                        }
                        else if (value.equals("Away")){
                            res.set(1, odd);
                        }
                    }
                }
            }
        }
        
        return res;
    }

    public Set<Participant> getGameParticipants(JSONObject game, String idLeague) {
        JSONObject teams = (JSONObject) game.get("teams");
        JSONObject home = (JSONObject) teams.get("home");
        JSONObject away = (JSONObject) teams.get("away");
        String homeTeam = (String) home.get("name");
        String awayTeam = (String) away.get("name");


        Set<Participant> ps = new HashSet<>();
        List<Float> odds = getOdds(getGameExternalId(game), idLeague);
        Participant homeP = new Participant(homeTeam, odds.get(0), 0);
        Participant awayP = new Participant(awayTeam, odds.get(1), 0);

        ps.add(homeP); ps.add(awayP);

        return ps;
    }

    public Set<Participant> updateParticipantsOdds(Set<Participant> participants, String home, String away, String game, String idLeague){
        Set<Participant> ps = new HashSet<>();
        List<Float> odds = getOdds(game, idLeague);

        for(Participant p: participants){
            if (p.getName().equals(home)){
                p.setOdd(odds.get(0));
            }
            else if (p.getName().equals(away)){
                p.setOdd(odds.get(1));
            }
        }

        return ps;
    }

    @Override
    public Set<Participant> updateOdds(List<Game> games) {
        Set<Participant> res = new HashSet<>();

        for(Game g: games){
            Set<Participant> ps = g.getParticipants();

            String home = extractTeamFromGameName(g.getTitle(), true);
            String away = extractTeamFromGameName(g.getTitle(), false);
            updateParticipantsOdds(ps, home, away, g.getExtID(), leagueID);

            for(Participant p: ps)
                res.add(p);
        }

        return res;
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

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

public class FootballAPISportsReader extends APIGameReader{
    private String sport_id;
    private int gamesToLoad = 10;
    private String response;

    public FootballAPISportsReader(String sport_id){
        ReadJSONBehaviour readMethod = new ReadJSONFromExternalAPI();
        super.setReadMethod(readMethod);
        this.sport_id = sport_id;
    }

    @Override
    public List<Game> getAPIGames() {
        String leagueID = "1";
        String season = "2022";

        String url = "https://v3.football.api-sports.io/fixtures?league=" + leagueID + "&season=" + season;
        response = super.readJSON(url, "jsons/jogos_wc.json", "b68a93e4291b512a0f3179eb9ee1bc2b");
        /*
        response = "";
        try {
            response = super.readFromLocalFile("jsons/jogos_wc.json");
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        JSONArray games = (JSONArray) (new JSONObject(response).get("response"));
        List<Game> res = new ArrayList<>();

        int added = 0;
        for(int i = 0; i < games.length() && added < gamesToLoad; i++){
            JSONObject obj = (JSONObject) games.get(i);
            JSONObject fixture = (JSONObject) obj.get("fixture");
            JSONObject status = (JSONObject) fixture.get("status");
            String status_short = (String) status.get("short");

            if ((status_short.equals("NS") || status_short.equals("TBD")) && added < this.gamesToLoad){
                Game g = new Game(getGameExternalId(obj), getGameDate(obj), getGameState(obj), getName(obj), getSportID(), getGameParticipants(obj, "1"));
                res.add(g);
                added++;
            }
        }

        return res;
    }

    public String getGameExternalId(JSONObject game) {
        JSONObject fixture = (JSONObject) game.get("fixture");
        return fixture.get("id").toString();
    }

    public LocalDateTime getGameDate(JSONObject game) {
        JSONObject fixture = (JSONObject) game.get("fixture");
        String date = (String) fixture.get("date");

        ZonedDateTime zdt = ZonedDateTime.parse(date);
        LocalDateTime dateTime = zdt.toLocalDateTime();
        return dateTime;
    }

    public List<Float> getOdds(String extID, String idLeague){
        String url = "https://v3.football.api-sports.io/odds?season=2022&bet=1&fixture=" + extID + "&league=" + idLeague;

        String fixtureResponse = super.readJSON(url, "jsons/football/" + idLeague + "_" + extID + ".json", "b68a93e4291b512a0f3179eb9ee1bc2b");
        //String fixtureResponse = "";
        //try {
        //    fixtureResponse = super.readFromLocalFile("jsons/football/" + idLeague + "_" + getGameExternalId(game));
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        JSONArray oddsFixture = (JSONArray) (new JSONObject(fixtureResponse).get("response"));
        List<Float> res = new ArrayList<>();

        if(oddsFixture.length() > 0){
            JSONObject obj = (JSONObject) oddsFixture.get(0);
            JSONArray bookmakers = obj.getJSONArray("bookmakers");
            JSONObject fstBookmaker = (JSONObject) bookmakers.get(0);
            JSONArray bets = fstBookmaker.getJSONArray("bets");
            JSONObject fstBet = (JSONObject) bets.get(0);
            JSONArray values = (JSONArray) fstBet.get("values");

            for(int i = 0; i < 3; i++){
                JSONObject value = (JSONObject) values.get(i);
                res.add(Float.parseFloat((String) value.get("odd")));
            }
        }
        else{
            float tmp = 1.0f;
            res.add(tmp); res.add(tmp); res.add(tmp); 
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
        Participant drawP = new Participant("draw", odds.get(1), 0);
        Participant awayP = new Participant(awayTeam, odds.get(2), 0);

        ps.add(homeP); ps.add(drawP); ps.add(awayP);

        return ps;
    }

    public void updateParticipantsOdds(Set<Participant> participants, String home, String away, String game, String idLeague) {
        List<Float> odds = getOdds(game, idLeague);

        for(Participant p: participants){
            if (p.getName().equals(home)){
                p.setOdd(odds.get(0));
            }
            else if (p.getName().equals(away)){
                p.setOdd(odds.get(2));
            }
            else{
                p.setOdd(odds.get(1));
            }
        }
    }

    public String getSportID() {
        return this.sport_id;
    }

    public int getGameState(JSONObject game) {
        JSONObject fixture = (JSONObject) game.get("fixture");
        JSONObject status = (JSONObject) fixture.get("status");
        String status_short = (String) status.get("short");

        if (status_short.equals("NS") || status_short.equals("1H") || status_short.equals("HT")
                    || status_short.equals("2H") || status_short.equals("ET") || status_short.equals("P") || status_short.equals("TBD"))
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

        return homeTeam + " vs " + awayTeam;
    }

    public Set<Participant> updateOdds(List<Game> games){
        Set<Participant> res = new HashSet<>();

        for(Game g: games){
            Set<Participant> ps = g.getParticipants();

            String home = extractTeamFromGameName(g.getTitle(), true);
            String away = extractTeamFromGameName(g.getTitle(), false);
            updateParticipantsOdds(ps, home, away, g.getExtID(), "1");

            for(Participant p: ps)
                res.add(p);
        }

        return res;
    }

    public Set<Participant> updateScores(List<Game> games){
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

                    JSONObject score = (JSONObject) obj.get("goals");

                    if (score.get("home") != null){
                        for(Participant p: ps){
                            if (p.getName().equals(home)){
                                int goals = (int) score.get("home");
                                p.setScore(goals);
                            }
                            else if (p.getName().equals(away)){
                                int goals = (int) score.get("away");
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

    public List<Game> updateGamesState(List<Game> games){
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
        String[] teams = name.split(" vs ");
        if (home)
            return teams[0];
        else
            return teams[1];
    }
}

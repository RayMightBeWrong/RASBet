package ras.adlrr.RASBet.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import ras.adlrr.RASBet.dao.GameDAO;
import ras.adlrr.RASBet.dao.ParticipantDAO;
import ras.adlrr.RASBet.model.APIGameReader;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Participant;
import ras.adlrr.RASBet.model.readers.FootballAPISportsReader;

@Service
public class GameService {
    private final GameDAO gameDAO;
    private final ParticipantDAO participantDAO;

    /* **** Game Methods **** */
    @Autowired
    public GameService(@Qualifier("FakeGameDAO") GameDAO gameDAO, @Qualifier("FakeParticipantDAO") ParticipantDAO participantDAO){
        this.gameDAO = gameDAO;
        this.participantDAO = participantDAO;
    }

    public List<Game> getGames() {
        //updateGames();
        updateGames2();
        return gameDAO.getGames();
    }

    public Game getGame(int id){
        return gameDAO.getGame(id);
    }

    public int addGame(Game g) {
        return gameDAO.addGame(g);
    }

    public int changeGameState(int id, String state){
        return gameDAO.changeGameState(id, state);
    }

    public int changeGameDate(int id, LocalDateTime date) {
        return gameDAO.changeGameDate(id,date);
    }

    // TODO: fazer uma função de jeito
    public int updateGames(){
        String json = readJSONfromHTTPRequest("http://ucras.di.uminho.pt/v1/games/");
        
        if (json == null){
            return 0;
        }

        JSONArray ja = new JSONArray(json);
        for(int i = 0; i < ja.length(); i++){
            JSONObject jo = (JSONObject) ja.get(i);
            
            String iso8601 = (String) jo.get("commenceTime");
            ZonedDateTime zdt = ZonedDateTime.parse(iso8601);
            LocalDateTime ldt = zdt.toLocalDateTime();

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
            System.out.println();
            Participant home = new Participant(homeTeam, homeOdd, 0);
            Participant away = new Participant(awayTeam, awayOdd, 0);
            Participant draw = new Participant("draw", drawOdd, 0);
            List<Participant> ps = new ArrayList<>();
            ps.add(home); ps.add(away); ps.add(draw);

            // TODO: mudar para diferentes desportos e mudar o resultado
            // TODO: get ID do desporto
            Game g = new Game(5, (String) jo.get("id"), ldt, ps, 1, Game.CLOSED);
            gameDAO.addGame(g);
        }
        
        return 1;
    }

    public void updateGames2(){
        //HttpResponse<String> response = Unirest.get("https://v3.football.api-sports.io/odds?season=2022&bet=1&fixture=898685&league=94")
        //                                    .header("x-rapidapi-key", "b68a93e4291b512a0f3179eb9ee1bc2b")
        //                                    .header("x-rapidapi-host", "v3.football.api-sports.io").asString();
        //try {
        //    Files.write( Paths.get("/home/ray/odd_fixture.json"), response.getBody().getBytes());
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}
        try{
            BufferedReader br1 = new BufferedReader(new FileReader(new File("/home/ray/jogos.json")));
            StringBuilder sb1 = new StringBuilder();
            
            String st;
            while ((st = br1.readLine()) != null)
                sb1.append(st);

            APIGameReader reader = new FootballAPISportsReader(sb1.toString(), gameDAO);
            reader.loadGames();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public String readJSONfromHTTPRequest(String urlString){
        try{
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            int rCode = connection.getResponseCode();
            if (rCode != 200){
                throw new RuntimeException("HTTP Response Code: " + rCode);
            }
            
            StringBuilder sb = new StringBuilder();
            Scanner scanner = new Scanner(url.openStream());
            while (scanner.hasNext()){
                sb.append(scanner.nextLine());        
            }

            scanner.close();
            return sb.toString();
        }
        catch (MalformedURLException e){
            return null;
        }
        catch (IOException e){
            return null;
        }
        catch (RuntimeException e){
            return null;
        }
    }


    /* **** Participants Methods **** */
    public List<Participant> getGameParticipants(int gameID) {
        return participantDAO.getGameParticipants(gameID);
    }

    public int addParticipantToGame(int gameID, Participant p) {
        return participantDAO.addParticipantToGame(gameID, p);
    }
}

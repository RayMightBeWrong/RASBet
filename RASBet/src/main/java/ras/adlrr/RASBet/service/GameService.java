package ras.adlrr.RASBet.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import ras.adlrr.RASBet.dao.GameRepository;
import ras.adlrr.RASBet.dao.ParticipantRepository;
import ras.adlrr.RASBet.dao.SportRepository;
import ras.adlrr.RASBet.model.APIGameReader;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Participant;
import ras.adlrr.RASBet.model.Sport;
import ras.adlrr.RASBet.model.readers.F1APISportsReader;
import ras.adlrr.RASBet.model.readers.FootballAPISportsReader;
import ras.adlrr.RASBet.model.readers.NFLOddsAPIReader;

import javax.servlet.http.Part;

@Service
public class GameService {
    private final GameRepository gr;
    private final ParticipantRepository pr;
    private final SportRepository sr;


    @Autowired
    public GameService(GameRepository gameRepository, ParticipantRepository participantRepository, SportRepository sportRepository){
        this.gr = gameRepository;
        this.pr = participantRepository;
        this.sr = sportRepository;
    }

    /* **** Game Methods **** */

    //TODO - criar metodo q filtra os jogos q já terminaram
    public List<Game> getGames() {
        //updateGames();
        //updateGames2();
        return gr.findAll();
    }

    public Game getGame(int id){
        return gr.findById(id).orElse(null);
    }

    public Game addGame(Game newGame) throws Exception {
        Game game = gr.findByExtID(newGame.getExtID()).orElse(null);

        if(game != null)
            throw new Exception("Game with the given external id already exists!");
        if (!(sr.existsById(newGame.getSport().getId())))
            throw new Exception("Cannot add a game to an invalid sport!");
        if (newGame.getState() != Game.CLOSED && newGame.getState() != Game.OPEN && newGame.getState() != Game.SUSPENDED)
            throw new Exception("Game state not recognized!");

        Set<Participant> participants = newGame.getParticipants();
        if(participants != null)
            validateParticipants(participants);

        gr.save(newGame);

        return newGame;
    }

    public void removeGame(int id) throws Exception {
        if (!gr.existsById(id))
            throw new Exception("Game needs to exist to be removed!");
        gr.deleteById(id);
    }

    /* **** Participants Methods **** */

    private void validateParticipants(Collection<Participant> participants) throws Exception {
        if(participants == null || participants.stream().noneMatch(Objects::nonNull))
            throw new Exception("No participants were given!");

        for(Participant p : participants){
            if(p == null)
                throw new Exception("Participant cannot be null!");
            p.setId(0); // Certifica q n dá erro por ter sido mencionado um id
            if(p.getOdd() < 1)
                throw new Exception("Odds must be equal or higher to/than 1!");
        }
    }

    public Set<Participant> getGameParticipants(int gameID) throws Exception {
        Game game = gr.loadGameById(gameID).orElse(null);
        if(game == null)
            throw new Exception("Cannot get participants from an invalid game!");
        return game.getParticipants();
    }

    public void addParticipantsToGame(int gameID, Collection<Participant> participants) throws Exception {
            Game game = gr.loadGameById(gameID).orElse(null);
            if(game == null)
                throw new Exception("Cannot add participants to non existent game!");

            validateParticipants(participants);

            for(Participant p : participants)
                game.addParticipantToGame(p);

            gr.save(game);
    }

    public void addParticipantToGame(int gameID, Participant p) throws Exception {
        addParticipantsToGame(gameID, List.of(p));
    }

    //TODO - fazer verificacao com o id do jogo
    public void removeParticipantFromGame(int gameID, int participant_id) throws Exception {
        Participant participant = pr.findById(participant_id).orElse(null);
        if (participant != null)
            pr.deleteById(participant_id);
        else
            throw new Exception("Cannot remove participant that does not exist!");
    }

    public Participant getParticipantFromGame(int participant_id){
        return pr.findById(participant_id).orElse(null);
    }



    /*  UPDATE DE JOGOS */

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
            //Game g = new Game(5, (String) jo.get("id"), ldt, ps, 1, Game.CLOSED);
            //TODO
            //gameRepository.addGame(g);
        }

        return 1;
    }

    public void updateGames2(){

        //HttpResponse<String> response = Unirest.get("https://api.the-odds-api.com/v4/sports/americanfootball_nfl/odds/?regions=us&oddsFormat=american&apiKey=70d50d68d47a79f93f43e9d7353e16ed")
        //                                    .header("x-rapidapi-key", "b68a93e4291b512a0f3179eb9ee1bc2b")
        //                                    .header("x-rapidapi-host", "v3.football.api-sports.io").asString();
        //try {
        //    Files.write( Paths.get("/home/ray/nfl.json"), response.getBody().getBytes());
        //} catch (Exception e) {
        //    e.printStackTrace();
        //}


        try{
            BufferedReader br1 = new BufferedReader(new FileReader(new File("/home/ray/nfl.json")));
            StringBuilder sb1 = new StringBuilder();

            String st;
            while ((st = br1.readLine()) != null)
                sb1.append(st);

            //APIGameReader reader = new FootballAPISportsReader(sb1.toString(), gameRepository);
            //reader.loadGames();
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

}

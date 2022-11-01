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
import ras.adlrr.RASBet.model.readers.UcrasAPIReader;


@Service
public class GameService {
    private final GameRepository gr;
    private final ParticipantRepository pr;
    private final SportService sportService;


    @Autowired
    public GameService(GameRepository gameRepository, ParticipantRepository participantRepository, SportService sportService){
        this.gr = gameRepository;
        this.pr = participantRepository;
        this.sportService = sportService;
    }

    /* **** Game Methods **** */
    
    public List<Game> getGames() {
        return gr.findAll();
    }

    public List<Game> getOngoingGames(){
        List<Game> games = getGames();
        List<Game> res = new ArrayList<>();

        for(int i = 0; i < games.size(); i++)
            if (games.get(i).getState() != Game.CLOSED)
                res.add(games.get(i));

        return res;
    }

    public void updateGames() throws Exception{
        //getGamesFromAPILocal();
        getGamesFromAPI();
    }

    public Game getGame(int id){
        return gr.findById(id).orElse(null);
    }

    public Game addGame(Game newGame) throws Exception {
        Game game = gr.findByExtID(newGame.getExtID()).orElse(null);

        if (game != null)
            throw new Exception("Game with the given external id already exists!");

        Sport sport = newGame.getSport();
        if (sport == null || !(sportService.sportExistsById(sport.getId())))
            throw new Exception("Cannot add a game to an invalid sport!");

        if (newGame.getState() != Game.CLOSED && newGame.getState() != Game.OPEN && newGame.getState() != Game.SUSPENDED)
            throw new Exception("Game state not recognized!");

        Set<Participant> participants = newGame.getParticipants();
        if (participants != null)
            validateParticipants(participants);

        gr.save(newGame);
        return newGame;
    }

    public void addGames(List<Game> games) throws Exception{
        for (int i = 0; i < games.size(); i++)
            addGame(games.get(i));
    }

    public void removeGame(int id) throws Exception {
        if (!gr.existsById(id))
            throw new Exception("Game needs to exist to be removed!");
        gr.deleteById(id);
    }

    public void changeGameState(int id, int state) throws Exception{
        if (state != Game.CLOSED && state != Game.OPEN && state != Game.SUSPENDED)
            throw new Exception("Game state to be inserted is not valid!");
        
        Game game = gr.findById(id).orElse(null);
        if (game == null)
            throw new Exception("Game doesn't exist!");

        game.setState(state);
        gr.save(game);
    }

    public boolean gameExistsById(int id) {
        return gr.existsById(id);
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

    public void removeParticipant(int participant_id) throws Exception {
        Participant participant = pr.findById(participant_id).orElse(null);
        if (participant != null)
            pr.deleteById(participant_id);
        else
            throw new Exception("Cannot remove participant that does not exist!");
    }

    public Participant getParticipant(int participant_id){
        return pr.findById(participant_id).orElse(null);
    }

    public void editOddInParticipant(int participant_id, float odd) throws Exception {
        if (odd <= 1)
            throw new Exception("Inserted odd is not valid!");

        Participant participant = pr.findById(participant_id).orElse(null);
        if (participant != null){
            participant.setOdd(odd);
            pr.save(participant);
        }
        else
            throw new Exception("Could not find participant!");
    }

    public boolean participantExistsById(int id) {
        return pr.existsById(id);
    }

    /*  UPDATE DE JOGOS */
    
    // TODO: fazer uma função de jeito
    public void getGamesFromAPILocal() throws Exception{
        Sport sport = sportService.findSportByName("Futebol");
        if (sport != null){
            APIGameReader reader = new UcrasAPIReader(sport.getId());
            //APIGameReader reader = new NFLOddsAPIReader(response.getBody(), sport.getId());

            List<Game> games = reader.getAPIGames();
            if (games == null)
                throw new Exception("Error occurred while reading request from external API");
            
            addGames(games);
        }
    }

    public void getGamesFromAPI() throws Exception{
        /*
        HttpResponse<String> response = Unirest.get("https://api.the-odds-api.com/v4/sports/americanfootball_nfl/odds/?regions=us&oddsFormat=american&apiKey=70d50d68d47a79f93f43e9d7353e16ed")
                                            .header("x-rapidapi-key", "b68a93e4291b512a0f3179eb9ee1bc2b")
                                            .header("x-rapidapi-host", "v3.football.api-sports.io").asString();
        try {
            Files.write( Paths.get("/home/ray/nfl.json"), response.getBody().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        BufferedReader br1 = new BufferedReader(new FileReader(new File("/home/ray/nfl.json")));
        StringBuilder sb1 = new StringBuilder();
        String st;

        while ((st = br1.readLine()) != null)
            sb1.append(st);

        Sport sport = sportService.findSportByName("NFL");
        if (sport != null){
            //sb1.toString();
            //APIGameReader reader = new NFLOddsAPIReader(response.getBody(), sport.getId());
            //List<Game> games = reader.getAPIGames();
            //addGames(games);
        }
    }
}

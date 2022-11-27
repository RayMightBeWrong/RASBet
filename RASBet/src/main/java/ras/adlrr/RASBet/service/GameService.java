package ras.adlrr.RASBet.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ras.adlrr.RASBet.dao.GameRepository;
import ras.adlrr.RASBet.dao.ParticipantRepository;
import ras.adlrr.RASBet.model.APIGameReader;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.GameChoice;
import ras.adlrr.RASBet.model.Participant;
import ras.adlrr.RASBet.model.Sport;
import ras.adlrr.RASBet.model.readers.F1APISportsReader;
import ras.adlrr.RASBet.model.readers.FootballAPISportsReader;
import ras.adlrr.RASBet.model.readers.NBAAPISportsReader;
import ras.adlrr.RASBet.model.readers.NFLOddsAPIReader;
import ras.adlrr.RASBet.model.readers.UcrasAPIReader;
import ras.adlrr.RASBet.service.interfaces.IGameService;
import ras.adlrr.RASBet.service.interfaces.IParticipantService;
import ras.adlrr.RASBet.service.interfaces.ISportService;


@Service
public class GameService implements IGameService, IParticipantService{
    private final GameRepository gr;
    private final ParticipantRepository pr;
    private final ISportService sportService;

    @Autowired
    public GameService(GameRepository gameRepository, ParticipantRepository participantRepository, ISportService sportService){
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
        String error = "";
        try { updateGamesNoVPN(); }
        catch (Exception e){ error += e.getMessage(); }
        //try{ updateGamesVPN(); }
        //catch (Exception e){ error += e.getMessage() ; }
        if(!error.equals(""))
            throw new Exception(error);
    }

    public void updateGamesVPN() throws Exception{
        getGamesFromAPIVPN();
    }

    public void updateGamesNoVPN() throws Exception{
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

    public void closeGame(int id) throws Exception{
        Game game = gr.loadGameById(id).orElse(null);
        if (game == null)
            throw new Exception("Game doesn't exist!");

        game.decideWinner();
        game.setState(Game.CLOSED);
        gr.save(game);
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
        Game game = gr.loadGameParticipantsById(gameID).orElse(null);
        if(game == null)
            throw new Exception("Cannot get participants from an invalid game!");
        return game.getParticipants();
    }

    public void addParticipantsToGame(int gameID, Collection<Participant> participants) throws Exception {
        Game game = gr.loadGameParticipantsById(gameID).orElse(null);
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

    public void giveOddToGameChoice(GameChoice gc){
        int pid = gc.getParticipant().getId();
        Participant p = getParticipant(pid);
        gc.setOdd(p.getOdd());
    }

    public void giveOddToGameChoices(List<GameChoice> gcs){
        for(GameChoice gc: gcs)
            giveOddToGameChoice(gc);
    }

    /*  UPDATE DE JOGOS */
    
    public void getGamesFromAPIVPN() throws Exception{
        Sport sport = sportService.findSportById("Football");
        if (sport != null){
            APIGameReader reader = new UcrasAPIReader(sport.getId());
            
            List<Game> games = reader.getAPIGames();
            if (games == null)
                throw new Exception("Error while updating football games (UCRAS). ");
            
            addGames(games);
        }
    }

    public void getGamesFromAPI() throws Exception{
        String errorMsg = "";
        Sport sport;



        /*
        try {
            Sport sport = sportService.findSportById("NFL");
            if (sport != null){
                APIGameReader reader = new NFLOddsAPIReader(sport.getId());
                List<Game> games = reader.getAPIGames();
                addGames(games);
            }
        }catch (Exception e){
            errorMsg = errorMsg + "Error while updating NFL games" + " | ";
        }
        */

        try {
            sport = sportService.findSportById("F1");
            if (sport != null){
                APIGameReader reader = new F1APISportsReader(sport.getId());
                List<Game> games = reader.getAPIGames();
                addGames(games);
            }
        }catch (Exception e){
            errorMsg = errorMsg + "Error while updating F1 games. ";
        }

        try {
            sport = sportService.findSportById("Football");
            if (sport != null){
                APIGameReader reader = new FootballAPISportsReader(sport.getId());
                List<Game> games = reader.getAPIGames();
                addGames(games);
            }
        }catch (Exception e){
            errorMsg = errorMsg + "Error while updating Football games. ";
            e.printStackTrace();
        }

        try {
            sport = sportService.findSportById("NBA");
            if (sport != null){
                APIGameReader reader = new NBAAPISportsReader(sport.getId(), 10);
                List<Game> games = reader.getAPIGames();
                addGames(games);
            }
        }catch (Exception e){
            errorMsg = errorMsg + "Error while updating NBA games. ";
            e.printStackTrace();
        }

        if(!errorMsg.equals(""))
            throw new Exception(errorMsg);
    }
}

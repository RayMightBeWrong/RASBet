package ras.adlrr.RASBet.service.sports;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.GameRepository;
import ras.adlrr.RASBet.dao.ParticipantRepository;
import ras.adlrr.RASBet.model.*;
import ras.adlrr.RASBet.model.readers.*;
import ras.adlrr.RASBet.service.interfaces.sports.IGameService;
import ras.adlrr.RASBet.service.interfaces.sports.IParticipantService;
import ras.adlrr.RASBet.service.interfaces.sports.ISportService;

import java.time.LocalDateTime;
import java.util.*;

@Service("gameService")
public class GameService implements IGameService, IParticipantService {
    private final GameRepository gr;
    private final ParticipantRepository pr;
    private final ISportService sportService;

    @Autowired
    public GameService(GameRepository gr, ParticipantRepository pr, @Qualifier("sportService") ISportService sportService) {
        this.gr = gr;
        this.pr = pr;
        this.sportService = sportService;
    }

    public List<Game> getGames(){
        return gr.findAll();
    }

    public List<Game> getGamesSorted(){
        List<Game> games = gr.findAll();

        games.removeIf(g -> g.getState() != Game.OPEN);
        Collections.sort(games, (g1, g2) -> {
            LocalDateTime ldt1 = g1.getDate(), ldt2 = g2.getDate();
            int diff = ldt1.compareTo(ldt2);
            if(diff > 0)
                return 1;
            else if (diff < 0)
                return -1;
            else
                return g1.getId() - g2.getId();
        });

        return games;
    }

    public List<Game> getOngoingGames(){
        List<Game> games = getGames();
        List<Game> res = new ArrayList<>();

        for(int i = 0; i < games.size(); i++)
            if (games.get(i).getState() != Game.CLOSED)
                res.add(games.get(i));

        return res;
    }

    public List<Game> updateGames() throws Exception{
        List<Game> res = new ArrayList<>();
        String error = "";

        try { res = updateGamesNoVPN(); }
        catch (Exception e){ error += e.getMessage(); }
        try{ 
            List<Game> toAdd = updateGamesVPN(); 
            for(Game g: toAdd)
                res.add(g);
        }
        catch (Exception e){ error += e.getMessage() ; }
       
        if(!error.equals(""))
            throw new Exception(error);

        return res;
    }

    public List<Game> updateGamesVPN() throws Exception{
        return getGamesFromAPIVPN();
    }

    public List<Game> updateGamesNoVPN() throws Exception{
        return getGamesFromAPI();
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

    public boolean gameExistsByExtID(String extID) {
        return gr.existsByExtID(extID);
    }

    /**
     * @param sport_id Identification of the sport
     * @return list of games of a sport present in the repository
     */
    public List<Game> getGamesFromSport(String sport_id){
        return gr.loadAllBySportId(sport_id);
    }


    /* **** Participants Methods **** */
    private void validateParticipants(Collection<Participant> participants) throws Exception {
        if(participants == null || participants.stream().noneMatch(Objects::nonNull))
            throw new Exception("No participants were given!");

        for(Participant p : participants){
            if(p == null)
                throw new Exception("Participant cannot be null!");
            p.setId(0); // Certifica q n dá erro por ter sido mencionado um id
            if(p.getOdd() < 1 && p.getOdd() != 0)
                throw new Exception("Odds must be equal or higher to/than 1!");
        }
    }

    public Set<Participant> getGameParticipants(int gameID) throws Exception {
        if(!gameExistsById(gameID))
            throw new Exception("Cannot get participants from an invalid game!");
        return pr.findAllByGameId(gameID);
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

    public void editScoreInParticipant(int participant_id, int score) throws Exception {
        if (score < 0)
            throw new Exception("Inserted odd is not valid!");

        Participant participant = pr.findById(participant_id).orElse(null);
        if (participant != null){
            participant.setScore(score);
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

    public List<Game> getGamesFromAPIVPN() throws Exception{
        Sport sport = sportService.findSportById("Football");
        if (sport != null){
            APIGameReader reader = new UcrasAPIReader(sport.getId());
            
            List<Game> games = reader.getAPIGames();
            if (games == null)
                throw new Exception("Error while updating football games (UCRAS). ");
            
            addGames(games);
            return updateOngoingGames(reader, "Football");
        }

        return new ArrayList<>();
    }

    public List<Game> getGamesFromAPI() throws Exception{
        List<Game> res = new ArrayList<>();
        String errorMsg = "";
        Sport sport;

        try {
            sport = sportService.findSportById("NFL");
            if (sport != null){
                APIGameReader reader = new NFLAPISportsReader(sport.getId());
                List<Game> games = reader.getAPIGames();
                addGames(games);
                List<Game> toAdd = updateOngoingGames(reader, "NFL");
                for(Game g: toAdd)
                    res.add(g);
            }
        }catch (Exception e){
            errorMsg = errorMsg + "Error while updating NFL games" + " | ";
        } 

        try {
            sport = sportService.findSportById("F1");
            if (sport != null){
                APIGameReader reader = new F1APISportsReader(sport.getId());
                List<Game> games = reader.getAPIGames();
                addGames(games);
                List<Game> toAdd = updateOngoingGames(reader, "F1");
                for(Game g: toAdd)
                    res.add(g);
            }
        }catch (Exception e){
            errorMsg = errorMsg + "Error while updating F1 games. ";
        }

        try {
            sport = sportService.findSportById("NBA");
            if (sport != null){
                APIGameReader reader = new NBAAPISportsReader(sport.getId());
                List<Game> games = reader.getAPIGames();
                addGames(games);
                List<Game> toAdd = updateOngoingGames(reader, "NBA");
                for(Game g: toAdd)
                    res.add(g);
            }
        }catch (Exception e){
            errorMsg = errorMsg + "Error while updating NBA games. ";
            e.printStackTrace();
        }

        try {
            sport = sportService.findSportById("Football");
            if (sport != null){
                APIGameReader reader = new FootballAPISportsReader(sport.getId());
                List<Game> games = reader.getAPIGames();
                addGames(games);
                List<Game> toAdd = updateOngoingGames(reader, "Football");
                for(Game g: toAdd)
                    res.add(g);            
            }
        }catch (Exception e){
            errorMsg = errorMsg + "Error while updating Football games. ";
            e.printStackTrace();
        }

        if(!errorMsg.equals(""))
            throw new Exception(errorMsg);

        return res;
    }

    private List<Game> updateOngoingGames(APIGameReader reader, String idSport) throws Exception{
        updateScores(reader, idSport);
        List<Game> res = updateGamesState(reader, idSport);
        updateOdds(reader, idSport);
        return res;
    }

    private List<Game> updateGamesState(APIGameReader reader, String idSport) throws Exception{
        List<Game> games = getOngoinGamesFromSport(idSport);
        List<Game> gamesToUpdate  = reader.updateGamesState(games);

        return gamesToUpdate;
    }

    private void updateScores(APIGameReader reader, String idSport) throws Exception{
        List<Game> gamesToUpdate = getOngoinGamesFromSport("Football");
        Set<Participant> ps = reader.updateScores(gamesToUpdate);

        for(Participant p: ps){
            editScoreInParticipant(p.getId(), p.getScore());
        }
    }

    private void updateOdds(APIGameReader reader, String idSport) throws Exception{
        List<Game> gamesToUpdate = getGamesWithoutOddsFromSport("Football");
        Set<Participant> ps = reader.updateOdds(gamesToUpdate);

        for(Participant p: ps){
            editOddInParticipant(p.getId(), p.getOdd());
        }
    }

    private List<Game> getOngoinGamesFromSport(String idSport){
        List<Game> res = getOngoingGames();
        res.removeIf(g -> !g.getSport().getId().equals(idSport));
        return res;
    }

    private List<Game> getGamesWithoutOddsFromSport(String idSport){
        List<Game> games = getGamesSorted();
        List<Game> res = new ArrayList<>();

        games.removeIf(g -> !g.getSport().getId().equals(idSport));
        for(Game g: games){
            Set<Participant> participants = g.getParticipants();

            boolean noOdds = true;
            for(Participant p: participants){
                if(p.getOdd() != 0){
                    noOdds = false;
                    break;
                }
            }
            if(noOdds){
                Game tmp = getGame(g.getId());
                if (tmp != null)
                    res.add(tmp);
            }
        }

        return res;
    }
}

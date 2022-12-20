package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.model.*;
import ras.adlrr.RASBet.service.game_subscription.IGameNotificationService;
import ras.adlrr.RASBet.service.game_subscription.IGameSubscriptionService;
import ras.adlrr.RASBet.service.interfaces.sports.IGameService;
import ras.adlrr.RASBet.service.interfaces.sports.IParticipantService;
import ras.adlrr.RASBet.service.interfaces.sports.ISportService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service("sportsFacade")
public class SportsFacade implements ISportService, IGameService, IParticipantService {
    private final ISportService sportService;
    private final IGameService gameService;
    private final IParticipantService participantService;

    @Autowired
    public SportsFacade(@Qualifier("sportService") ISportService sportService,
                        @Qualifier("gameService") IGameService gameService,
                        @Qualifier("gameService") IParticipantService participantService,
                        @Qualifier("gameNotificationService") IGameSubscriptionService gameSubscriptionService,
                        @Qualifier("gameNotificationService") IGameNotificationService gameNotificationService) {
        this.sportService = sportService;
        this.gameService = gameService;
        this.participantService = participantService;
    }

    /* ********* Sport Methods ********* */

    /**
     * Adds a sport to the repository
     * @param sport Sport to be persisted
     * @return sport updated by the repository
     * @throws Exception If any of the attributes does not meet the requirements an Exception is thrown indicating the error.
     */
    public Sport addSport(Sport sport) throws Exception {
        return sportService.addSport(sport);
    }

    /**
     * Checks for the existence of a sport with the given id. If the sport exists, returns it.
     * @param id Identification of the sport
     * @return sport if it exists, or null
     */
    public Sport findSportById(String id) {
        return sportService.findSportById(id);
    }

    /**
     * If a sport with the given id exists, removes it from the repository
     * @param id Identification of the sport
     * @throws Exception If the sport does not exist.
     */
    public void removeSport(String id) throws Exception {
        //TODO - remove em cascata
        sportService.removeSport(id);
    }

    /**
     * @return list of sports present in the repository
     */
    public List<Sport> getListOfSports() {
        return sportService.getListOfSports();
    }

    /**
     * Checks for the existence of a sport with the given id
     * @param id Identification of the sport
     * @return true if a sport exists with the given identification
     */
    public boolean sportExistsById(String id) {
        return sportService.sportExistsById(id);
    }

    /* ********* Game Methods ********* */

    public List<Game> getGames(){
        return gameService.getGames();
    }

    public List<Game> getGamesSorted(){
        return gameService.getGamesSorted();
    }

    public List<Game> getOngoingGames(){
        return gameService.getOngoingGames();
    }

    public List<Game> updateGames() throws Exception{
        return gameService.updateGames();
    }

    public Game getGame(int id){
        return gameService.getGame(id);
    }

    public Game addGame(Game game) throws Exception {
        if(game == null)
            throw new Exception("Game is null!");

        Sport sport = game.getSport();
        if (sport == null || !(sportService.sportExistsById(sport.getId())))
            throw new Exception("Cannot add a game to an invalid sport!");

        return gameService.addGame(game);
    }

    public void addGames(List<Game> games) throws Exception{
        gameService.addGames(games);
    }

    public void removeGame(int id) throws Exception {
        // TODO - preciso remover participantes e cancelar bets associadas
       gameService.removeGame(id);
    }

    @Transactional
    public void closeGame(int id) throws Exception{
        gameService.closeGame(id);
    }

    public void changeGameState(int id, int state) throws Exception{
        gameService.changeGameState(id,state);
    }

    public boolean gameExistsById(int id) {
        return gameService.gameExistsById(id);
    }


    /**
     * @param sport_id Identification of the sport
     * @return list of games of a sport present in the repository
     */
    public Set<Game> getGamesFromSport(String sport_id){
        return gameService.getGamesFromSport(sport_id);
    }


    /* **** Participants Methods **** */

    public Set<Participant> getGameParticipants(int gameID) throws Exception {
        return participantService.getGameParticipants(gameID);
    }

    public void addParticipantsToGame(int gameID, Collection<Participant> participants) throws Exception {
        participantService.addParticipantsToGame(gameID, participants);
    }

    public void addParticipantToGame(int gameID, Participant p) throws Exception {
        addParticipantsToGame(gameID, List.of(p));
    }

    public void removeParticipant(int participant_id) throws Exception {
        //TODO - no facade que envolve o bet service é necessario cancelar as bets
        participantService.removeParticipant(participant_id);
    }

    public Participant getParticipant(int participant_id){
        return participantService.getParticipant(participant_id);
    }

    @Transactional
    public void editOddInParticipant(int participant_id, float odd) throws Exception {
        participantService.editOddInParticipant(participant_id, odd);
    }

    public void editScoreInParticipant(int participant_id, int score) throws Exception {
        participantService.editScoreInParticipant(participant_id, score);
    }

    public boolean participantExistsById(int id) {
        return participantService.participantExistsById(id);
    }

    public void giveOddToGameChoice(GameChoice gc){
        participantService.giveOddToGameChoice(gc);
    }

    public void giveOddToGameChoices(List<GameChoice> gcs){
        participantService.giveOddToGameChoices(gcs);
    }

    public int getGameID(int participant_id){
        return participantService.getGameID(participant_id);
    }
}

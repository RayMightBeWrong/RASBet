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
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service("sportsFacade")
public class SportsFacade implements ISportService, IGameService, IParticipantService, IGameSubject, IGameSubscriptionService, IGameNotificationService {
    private final ISportService sportService;
    private final IGameService gameService;
    private final IParticipantService participantService;
    private final IGameSubscriptionService gameSubscriptionService;
    private final IGameNotificationService gameNotificationService;
    //TODO - Concurrency?
    private final Map<Integer, Set<Integer>> subscribersOfGames = new ConcurrentHashMap<>(); //Key: game id   |   Value: Set of gambler ids that subscribed the game
    private final Map<Integer, IGameSubscriber> subscribers = new ConcurrentHashMap<>(); //Key: gambler id   |   Value: Class associated with the gambler that should be notified

    @Autowired
    public SportsFacade(@Qualifier("sportService") ISportService sportService,
                        @Qualifier("gameService") IGameService gameService,
                        @Qualifier("gameService") IParticipantService participantService,
                        @Qualifier("gameNotificationService") IGameSubscriptionService gameSubscriptionService,
                        @Qualifier("gameNotificationService") IGameNotificationService gameNotificationService) {
        this.sportService = sportService;
        this.gameService = gameService;
        this.participantService = participantService;
        this.gameSubscriptionService = gameSubscriptionService;
        this.gameNotificationService = gameNotificationService;
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
        Participant participant = participantService.getParticipant(participant_id);
        int game_id = participantService.getGameID(participant_id);
        Game game = gameService.getGame(game_id);
        notifySubscribers(participantService.getGameID(participant_id), "Odd update", "Participant '" + participant.getName() + "' has now a odd of " + odd + " at event '" + game.getTitle() + "'.");
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

    /* ********* Subscription Methods ********* */

    /**
     * Registers the gambler has someone who wants to receive the notifications in real time.
     * @param gambler_id Identification of the gambler
     * @param gameSubscriber Subscriber that will await for updates
     */
    @Transactional
    public void subscribe(int gambler_id, IGameSubscriber gameSubscriber){
        var gameSubscriberAux = subscribers.get(gambler_id);

        //If there is already a subscription for the gambler, informs that instance that it wont receive the notification anymore.
        if(gameSubscriberAux != null)
            gameSubscriberAux.close();

        //Adds the gambler to the subscribers
        subscribers.put(gambler_id, gameSubscriber);

        //For all games followed by the gambler, adds him to the subscribers set
        var gamesSubscribed = gameSubscriptionService.findAllIdsOfGamesSubscribedByGambler(gambler_id);
        for (int game_id : gamesSubscribed){
            var set = subscribersOfGames.computeIfAbsent(game_id, k -> new HashSet<>());
            set.add(gambler_id);
        }
    }

    @Override
    @Transactional
    public GameSubscription subscribeGame(int gambler_id, int game_id){
        GameSubscription gs = gameSubscriptionService.subscribeGame(gambler_id, game_id);
        if(subscribers.containsKey(gambler_id)) {
            var set = subscribersOfGames.computeIfAbsent(game_id, k -> new HashSet<>());
            set.add(gambler_id);
        }
        return gs;
    }

    @Override
    public void unsubscribe(int gambler_id){
        subscribers.remove(gambler_id);
        for(var set : subscribersOfGames.values())
            set.remove(gambler_id);
    }

    @Override
    public void unsubscribeGame(int gambler_id, int game_id){
        gameSubscriptionService.unsubscribeGame(gambler_id, game_id);
        var set = subscribersOfGames.get(game_id);
        if(set != null) set.remove(gambler_id);
    }

    @Override
    public List<Integer> findAllIdsOfGamesSubscribedByGambler(int gamblerId) {
        return gameSubscriptionService.findAllIdsOfGamesSubscribedByGambler(gamblerId);
    }

    private void notifySubscribers(int game_id, String type, String message){
        Set<Integer> set = this.subscribersOfGames.get(game_id);
        if(set == null || type == null || message == null) return;

        for(Integer gambler_id: set){
            gameNotificationService.createGameNotification(gambler_id, type, message);
            IGameSubscriber gameSubscriber = subscribers.get(gambler_id);
            if(gameSubscriber != null) gameSubscriber.update(type, message);
        }
    }

    @Override
    public GameNotification createGameNotification(int gambler_id, String type, String msg) {
        return gameNotificationService.createGameNotification(gambler_id, type, msg);
    }

    @Override
    public List<GameNotification> findAllGameNotificationsByGamblerId(int gamblerId) {
        return gameNotificationService.findAllGameNotificationsByGamblerId(gamblerId);
    }
}

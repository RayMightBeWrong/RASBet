package ras.adlrr.RASBet.service;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.GameChoice;
import ras.adlrr.RASBet.model.Participant;

public interface IGameService {
    public List<Game> getGames();

    public List<Game> getOngoingGames();

    public void updateGamesVPN() throws Exception;

    public void updateGames() throws Exception;

    public Game getGame(int id);

    public Game addGame(Game newGame) throws Exception;

    public void addGames(List<Game> games) throws Exception;

    public void removeGame(int id) throws Exception;

    public void closeGame(int id) throws Exception;

    public void changeGameState(int id, int state) throws Exception;

    public boolean gameExistsById(int id);

    public Set<Participant> getGameParticipants(int gameID) throws Exception;

    public void addParticipantsToGame(int gameID, Collection<Participant> participants) throws Exception;

    public void addParticipantToGame(int gameID, Participant p) throws Exception;

    public void removeParticipant(int participant_id) throws Exception;

    public Participant getParticipant(int participant_id);

    public void editOddInParticipant(int participant_id, float odd) throws Exception;

    public boolean participantExistsById(int id);

    public void giveOddToGameChoice(GameChoice gc);

    public void giveOddToGameChoices(List<GameChoice> gcs);

    public void getGamesFromAPIVPN() throws Exception;

    public void getGamesFromAPI() throws Exception;

    
}

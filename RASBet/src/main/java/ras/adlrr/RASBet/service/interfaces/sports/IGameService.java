package ras.adlrr.RASBet.service.interfaces.sports;

import java.util.List;
import java.util.Set;

import ras.adlrr.RASBet.model.Game;

public interface IGameService {
    List<Game> getGames();

    List<Game> getGamesSorted();

    List<Game> getOngoingGames();

    Set<Game> getGamesFromSport(String sport);

    List<Game> updateGames() throws Exception;

    Game getGame(int id);

    Game addGame(Game newGame) throws Exception;

    void addGames(List<Game> games) throws Exception;

    void removeGame(int id) throws Exception;

    void closeGame(int id) throws Exception;

    void changeGameState(int id, int state) throws Exception;

    boolean gameExistsById(int id);
}

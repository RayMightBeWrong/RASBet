package ras.adlrr.RASBet.dao;

import ras.adlrr.RASBet.model.Game;
import java.time.LocalDateTime;
import java.util.List;

public interface GameDAO {
    List<Game> getGames();
    Game getGame(int id);
    int addGame(Game g);
    int changeGameState(int id, String state);
    int changeGameDate(int id, LocalDateTime date);
}

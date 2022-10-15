package ras.adlrr.RASBet.dao;

import ras.adlrr.RASBet.model.Game;

import java.time.LocalDate;

public interface GameDAO {
    Game getGame(int id);
    int addGame(Game g);
    int suspendGame(int id);
    int resumeGame(int id);
    int changeGameDate(int id, LocalDate date);
}

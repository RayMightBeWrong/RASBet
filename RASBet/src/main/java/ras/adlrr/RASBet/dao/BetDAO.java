package ras.adlrr.RASBet.dao;

import ras.adlrr.RASBet.model.Bet;

import java.util.List;

public interface BetDAO {
    int getBet(int id);
    int addBet(Bet bet);
    List<Bet> getUserBets(int userID);
}

package ras.adlrr.RASBet.service.interfaces;

import java.util.List;

import ras.adlrr.RASBet.model.Bet;
import ras.adlrr.RASBet.model.Transaction;

public interface IBetService {
    public Bet getBet(int id);

    public Bet addBet(Bet bet) throws Exception;

    public void removeBet(int betID) throws Exception;

    public List<Bet> getGamblerBets(int gambler_id) throws Exception;

    public Transaction withdrawBetWinnings(int bet_id, int wallet_id) throws Exception;
}

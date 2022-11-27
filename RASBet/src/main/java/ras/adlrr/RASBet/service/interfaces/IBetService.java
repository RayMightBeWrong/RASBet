package ras.adlrr.RASBet.service.interfaces;

import java.util.List;

import org.springframework.data.domain.Sort;
import ras.adlrr.RASBet.model.Bet;
import ras.adlrr.RASBet.model.Transaction;

public interface IBetService {
    Bet getBet(int id);

    Bet addBet(Bet bet) throws Exception;

    void removeBet(int betID) throws Exception;

    List<Bet> getGamblerBets(int gambler_id) throws Exception;

    List<Bet> getGamblerBets(int gambler_id, Sort.Direction direction) throws Exception;

    Transaction closeBetAndWithdrawWinnings(int bet_id) throws Exception;

    List<Bet> getBetsByGameId(int game_id) throws Exception;

    List<Integer> getBetsIdsByGameId(int game_id) throws Exception;
}

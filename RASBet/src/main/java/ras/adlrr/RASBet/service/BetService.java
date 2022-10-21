package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.BetDAO;
import ras.adlrr.RASBet.dao.BetRepository;
import ras.adlrr.RASBet.dao.TransactionRepository;
import ras.adlrr.RASBet.model.Bet;

import java.util.List;

@Service
public class BetService {
    /*
    private final BetDAO betDAO;

    @Autowired
    public BetService(@Qualifier("FakeBetDAO") BetDAO betDAO) {
        this.betDAO = betDAO;
    }

    public Bet getBet(int id) {
        return betDAO.getBet(id);
    }

    public int addBet(Bet bet) {
        return betDAO.addBet(bet);
    }

    public List<Bet> getUserBets(int userID) {
        return betDAO.getUserBets(userID);
    }

    public int removeBet(int betID) {
        return betDAO.removeBet(betID);
    }
    */

    private final BetRepository br;
    private final TransactionRepository tr;

    @Autowired
    public BetService(BetRepository br, TransactionRepository tr) {
        this.br = br;
        this.tr = tr;
    }

    public Bet getBet(int id) {
        return br.findById(id).orElse(null);
    }

    public int addBet(Bet bet) {
        bet.setTransaction(tr.findById(bet.getId()).orElse(null));
        br.save(bet);
        return 1;
    }


    public List<Bet> getUserBets(int userID) {
        return null;//return br.getUserBets(userID);
    }


    public int removeBet(int betID) {
        br.deleteById(betID);
        return 1;
    }
}
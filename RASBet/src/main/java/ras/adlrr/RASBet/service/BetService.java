package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.BetRepository;
import ras.adlrr.RASBet.dao.TransactionRepository;
import ras.adlrr.RASBet.model.Bet;

import java.util.List;

@Service
public class BetService {

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
        if (tr.existsById(bet.getId())) {
            br.save(bet);
            return 1;
        }else return 0;
    }


    public List<Bet> getUserBets(int userID) {
        return null;//return br.getUserBets(userID);
    }


    public int removeBet(int betID) {
        br.deleteById(betID);
        return 1;
    }
}
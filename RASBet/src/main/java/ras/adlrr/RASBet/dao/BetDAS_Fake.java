package ras.adlrr.RASBet.dao;

import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Bet;

import java.util.HashMap;
import java.util.List;

@Repository("FakeBetDAO")
public class BetDAS_Fake implements BetDAO{
    private static HashMap<Integer, Bet> bets;

    public Bet getBet(int id) {
        Bet b = bets.get(id);
        if(b == null)
            return null;
        else
            return new Bet(b);
    }

    @Override
    public int addBet(Bet bet) {
        if(bets.get(bet.getId()) != null){
            bets.put(bet.getId(), new Bet(bet));
            return 1;
        }
        return 0;
    }

    @Override
    public List<Bet> getUserBets(int userID) {
        var l = bets.values().stream().filter(b -> b.getGambler_id() == userID).map(Bet::new).toList();
        return l;
    }

    @Override
    public int removeBet(int betID) {
        Bet b = bets.get(betID);
        if(b == null)
            return 0;
        else
            return 1;
    }
}

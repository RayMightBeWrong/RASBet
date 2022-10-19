package ras.adlrr.RASBet.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Coin;

@Repository("coinDAO")
public interface CoinDAO {
    Coin getCoin(int id);
    int addCoin(Coin coin);
    int removeCoin(int id);
    List<Coin> getListOfCoins();
}

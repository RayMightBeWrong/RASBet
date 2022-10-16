package ras.adlrr.RASBet.dao;

import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Coin;

// DAS - Data Access Service
@Repository("coinDAO")
public class CoinDAS implements CoinDAO{
    private static HashMap<Integer, Coin> DB = new HashMap<>();
    private int id = 1;
    
    @Override
    public Coin getCoin(int id) {
        return DB.get(id);
    }
    
    @Override
    public int addCoin(Coin coin) {
        coin.setID(id);
        DB.put(id, coin);
        id++;
        return 1;
    }
    
    @Override
    public int removeCoin(int id) {
        DB.remove(id);
        return 1;
    }
    
    @Override
    public List<Coin> getListOfCoins() {
        return DB.values().stream().toList();
    }
}

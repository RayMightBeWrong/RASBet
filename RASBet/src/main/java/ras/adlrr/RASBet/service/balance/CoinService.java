package ras.adlrr.RASBet.service.balance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.CoinRepository;
import ras.adlrr.RASBet.model.Coin;
import ras.adlrr.RASBet.service.interfaces.balance.ICoinService;

import java.util.List;

@Service("coinService")
public class CoinService implements ICoinService {
    private final CoinRepository coinRepository;

    @Autowired
    public CoinService(CoinRepository coinRepository) {
        this.coinRepository = coinRepository;
    }

    /**
     * Checks for the existence of a coin with the given id. If the coin exists, returns it.
     * @param id Identification of the coin
     * @return coin if it exists, or null
     */
    public Coin getCoin(String id){
        return coinRepository.findById(id).orElse(null);
    }

    /**
     * Adds a coin to the repository
     * @param coin Coin to be persisted
     * @return coin updated by the repository
     * @throws Exception If any of the attributes does not meet the requirements an Exception is thrown indicating the error.
     */
    public Coin addCoin(Coin coin) throws Exception {
        if(coin.getRatio_EUR() <= 0)
            throw new Exception("The ratio cannot be 0 or negative.");
        if(coinExistsById(coin.getId()))
            throw new Exception("A coin with the given id already exists.");
        return coinRepository.save(coin);
    }

    /**
     * If a coin with the given id exists, removes it from the repository
     * @param id Identification of the coin
     * @throws Exception If the coin does not exist.
     */
    public void removeCoin(String id) throws Exception {
        if(!coinRepository.existsById(id))
            throw new Exception("Coin does not exist!");
        coinRepository.deleteById(id);
    }

    /**
     * @return list of coins present in the repository
     */
    public List<Coin> getListOfCoins(){
        return coinRepository.findAll();
    }

    /**
     * Checks for the existence of a coin with the given id
     * @param id Identification of the coin
     * @return true if a coin exists with the given identification
     */
    public boolean coinExistsById(String id) {
        return coinRepository.existsById(id);
    }

}

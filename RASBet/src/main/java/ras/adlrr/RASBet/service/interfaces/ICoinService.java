package ras.adlrr.RASBet.service.interfaces;

import java.util.List;

import ras.adlrr.RASBet.model.Coin;

public interface ICoinService {
    public Coin getCoin(String id);

    public Coin addCoin(Coin coin) throws Exception;

    public void removeCoin(String id) throws Exception;

    public List<Coin> getListOfCoins();

    public boolean coinExistsById(String id);
}

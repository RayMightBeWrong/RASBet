package ras.adlrr.RASBet.UI.ModelViews;

import ras.adlrr.RASBet.model.Coin;

public class CoinView {
    private Coin coin;

    public CoinView(Coin coin) {
        this.coin = coin;
    }

    public String toString(){
        return coin.getName() + " | Ratio de euro: " + coin.getRatio_EUR();
    }
}

package ras.adlrr.RASBet.UI.ModelViews;

import ras.adlrr.RASBet.model.Wallet;

public class WalletView {
    private Wallet wallet;

    public WalletView(Wallet wallet) {
        this.wallet = wallet;
    }

    public String toString(){
        CoinView coinView = new CoinView(wallet.getCoin());
        return "Carteira |" + "Dinheiro: " +  wallet.getBalance() + " " + coinView.toString() + " |";
    }
}

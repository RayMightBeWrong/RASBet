package ras.adlrr.RASBet.service;

import java.util.List;

import ras.adlrr.RASBet.model.Coin;
import ras.adlrr.RASBet.model.Wallet;

public interface IWalletService {
    public Wallet getWallet(int id);

    public List<Wallet> getGamblerWallets(int gambler_id);

    public List<Wallet> getListOfWallets();

    public Wallet createWallet(Wallet wallet) throws Exception;

    public void removeWallet(int id) throws Exception;

    public Wallet addToBalance(int wallet_id, float valueToAdd) throws Exception;

    public Wallet removeFromBalance(int wallet_id, float valueToRemove) throws Exception;

    public boolean walletExistsById(int id);

    public Integer findGamblerIdByWalletId(int wallet_id);

    public String getCoinIdFromWallet(int wallet_id);
}

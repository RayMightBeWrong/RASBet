package ras.adlrr.RASBet.dao;

import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import ras.adlrr.RASBet.model.Wallet;

// DAS - Data Access Service
@Repository("walletDAO")
public class WalletDAS implements WalletDAO{
    private static HashMap<Integer, Wallet> DB = new HashMap<>();
    private int id = 1;
    
    @Override
    public Wallet getWallet(int id) {
        return DB.get(id);
    }
    
    @Override
    public int addWallet(Wallet wallet) {
        wallet.setID(id);
        DB.put(id, wallet);
        id++;
        return 1;
    }
    
    @Override
    public int removeWallet(int id) {
        DB.remove(id);
        return 1;
    }
    
    @Override
    public List<Wallet> getListOfWallets() {
        return DB.values().stream().toList();
    }
}

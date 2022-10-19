package ras.adlrr.RASBet.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Wallet;

@Repository("walletDAO")
public interface WalletDAO {
    Wallet getWallet(int id);
    int addWallet(Wallet wallet);
    int removeWallet(int id);
    int updateWallet(Wallet wallet);
    List<Wallet> getListOfWallets();
}

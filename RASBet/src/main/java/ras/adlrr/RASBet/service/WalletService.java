package ras.adlrr.RASBet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ras.adlrr.RASBet.dao.*;
import ras.adlrr.RASBet.model.Coin;
import ras.adlrr.RASBet.model.Wallet;

@Service
public class WalletService {
    /*
    private final WalletDAO walletDAO;
    private final CoinDAO coinDAO;

    @Autowired
    public WalletService(@Qualifier("walletDAO") WalletDAO walletDAO, @Qualifier("coinDAO") CoinDAO coinDAO){
        this.walletDAO = walletDAO;
        this.coinDAO = coinDAO;
    }

    public Wallet getWallet(int id){
        return walletDAO.getWallet(id);
    }

    public int addWallet(Wallet wallet){
        //TODO: check se moeda existe
        return walletDAO.addWallet(wallet);
    }
    
    public int removeWallet(int id){
        return walletDAO.removeWallet(id);
    }

    public List<Wallet> getListOfWallets(){
        return walletDAO.getListOfWallets();
    }

    public Coin getCoin(int id){
        return coinDAO.getCoin(id);
    }

    public int addCoin(Coin coin){
        return coinDAO.addCoin(coin);
    }

    public int removeCoin(int id){
        return coinDAO.removeCoin(id);
    }

    public List<Coin> getListOfCoins(){
        return coinDAO.getListOfCoins();
    }
    */
    private final WalletRepository walletRepository;
    private final CoinRepository coinRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository, CoinRepository coinRepository){
        this.walletRepository = walletRepository;
        this.coinRepository = coinRepository;
    }

    public Wallet getWallet(int id){
        return walletRepository.findById(id).orElse(null);
    }

    public int addWallet(int coinId, Wallet wallet){
        Coin coin = coinRepository.findById(coinId).orElse(null);
        if(coin==null) return 0;
        wallet.setCoin(coin);
        walletRepository.save(wallet);
        return 1;
    }

    public int removeWallet(int id){
        walletRepository.deleteById(id);
        return 1;
    }

    public List<Wallet> getListOfWallets(){
        return walletRepository.findAll();
    }

    public Coin getCoin(int id){
        return coinRepository.findById(id).orElse(null);
    }

    public int addCoin(Coin coin){
        coinRepository.save(coin);
        return 1;
    }

    public int removeCoin(int id){
        coinRepository.deleteById(id);
        return 1;
    }

    public List<Coin> getListOfCoins(){
        return coinRepository.findAll();
    }
}

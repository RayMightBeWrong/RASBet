package ras.adlrr.RASBet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ras.adlrr.RASBet.dao.*;
import ras.adlrr.RASBet.model.Coin;
import ras.adlrr.RASBet.model.Gambler;
import ras.adlrr.RASBet.model.Transaction;
import ras.adlrr.RASBet.model.Wallet;

@Service
public class WalletService {
    private final UserService userService;
    private final WalletRepository walletRepository;
    private final CoinRepository coinRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository, CoinRepository coinRepository, UserService userService){
        this.walletRepository = walletRepository;
        this.coinRepository = coinRepository;
        this.userService = userService;
    }


    // ---------- Wallet Methods ----------

    public Wallet getWallet(int id){
        return walletRepository.findById(id).orElse(null);
    }

    public List<Wallet> getGamblerWallets(int gambler_id){
        return walletRepository.findAllByGamblerId(gambler_id);
    }

    public List<Wallet> getListOfWallets(){
        return walletRepository.findAll();
    }

    public Wallet createWallet(Wallet wallet) throws Exception {
        wallet.setId(0);

        Coin coin = wallet.getCoin();
        if(coin == null || !coinExistsById(coin.getId()))
            throw new Exception("Cannot create wallet for an invalid coin!");

        Gambler gambler = wallet.getGambler();
        if(gambler == null || !userService.gamblerExistsById(gambler.getId()))
            throw new Exception("Cannot create wallet for an invalid gambler!");

        return walletRepository.save(wallet);
    }

    public void removeWallet(int id) throws Exception {
        if(!walletExistsById(id))
            throw new Exception("Wallet needs to exist to be removed!");
        walletRepository.deleteById(id);
    }

    public Wallet addToBalance(int wallet_id, float valueToAdd) throws Exception {
        if (valueToAdd < 0) throw new Exception("Cannot perform the operation with a negative value!");
        return changeBalance(wallet_id, valueToAdd);
    }

    public Wallet removeFromBalance(int wallet_id, float valueToRemove) throws Exception {
        if (valueToRemove < 0) throw new Exception("Cannot perform the operation with a negative value!");
        return changeBalance(wallet_id, -valueToRemove);
    }

    private Wallet changeBalance(int wallet_id, float value) throws Exception {
        Wallet wallet = walletRepository.findById(wallet_id).orElse(null);
        if(wallet == null)
            throw new Exception("Cannot perform deposit operation to a non existent wallet!");
        if(!wallet.changeBalance(value))
            throw new Exception("Wallet does not have sufficient funds to execute the withdraw operation!");
        return walletRepository.save(wallet);
    }

    public boolean walletExistsById(int id) {
        return walletRepository.existsById(id);
    }

    public Integer findGamblerIdByWalletId(int wallet_id){
        return walletRepository.findGamblerIdByWalletId(wallet_id).orElse(null);
    }

    // ---------- Coin Methods ----------

    public Coin getCoin(int id){
        return coinRepository.findById(id).orElse(null);
    }

    public Coin addCoin(Coin coin){
        coin.setId(0);
        return coinRepository.save(coin);
    }

    public void removeCoin(int id) throws Exception {
        if(!coinRepository.existsById(id))
            throw new Exception("A coin needs to exist to be removed!");
        coinRepository.deleteById(id);
    }

    public List<Coin> getListOfCoins(){
        return coinRepository.findAll();
    }

    public boolean coinExistsById(int id) {
        return coinRepository.existsById(id);
    }
}

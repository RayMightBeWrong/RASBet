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
    private final TransactionService transactionService;
    private final UserService userService;
    private final WalletRepository walletRepository;
    private final CoinRepository coinRepository;

    @Autowired
    public WalletService(TransactionService transactionService, WalletRepository walletRepository, CoinRepository coinRepository, UserService userService){
        this.transactionService = transactionService;
        this.walletRepository = walletRepository;
        this.coinRepository = coinRepository;
        this.userService = userService;
    }


    // ---------- Wallet Methods ----------

    public Wallet getWallet(int id){
        return walletRepository.findById(id).orElse(null);
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

    public List<Wallet> getListOfWallets(){
        return walletRepository.findAll();
    }

    public Wallet deposit(int wallet_id, float valueToDeposit) throws Exception {
        Wallet wallet = changeBalance(wallet_id, valueToDeposit);

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setValue(valueToDeposit);
        transaction.setDescription("Deposit");
        transaction.setBalance_after_mov(wallet.getBalance());

        Integer gambler_id = walletRepository.findGamblerIdByWalletId(wallet_id).orElse(null);
        if(gambler_id == null)
            throw new Exception("A gambler does not exist for the given wallet! This should not be happening...");
        Gambler gambler = new Gambler();
        gambler.setId(gambler_id);
        transaction.setGambler(gambler);

        transactionService.addTransaction(transaction);

        return wallet;
    }

    public Wallet withdraw(int wallet_id, float valueToWithdraw) throws Exception {
        Wallet wallet = changeBalance(wallet_id, -valueToWithdraw);

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setValue(valueToWithdraw);
        transaction.setDescription("Withdraw");
        transaction.setBalance_after_mov(wallet.getBalance());

        Integer gambler_id = walletRepository.findGamblerIdByWalletId(wallet_id).orElse(null);
        if(gambler_id == null)
            throw new Exception("A gambler does not exist for the given wallet! This should not be happening...");
        Gambler gambler = new Gambler();
        gambler.setId(gambler_id);
        transaction.setGambler(gambler);

        transactionService.addTransaction(transaction);

        return wallet;
    }

    public Wallet performBilling(int wallet_id, float valueToBill) throws Exception {
        return changeBalance(wallet_id, -valueToBill);
    }

    private Wallet changeBalance(int wallet_id, float value) throws Exception {
        if (value < 0) throw new Exception("Cannot perform the operation with a negative value!");
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

package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.*;
import ras.adlrr.RASBet.model.*;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final WalletService walletService;
    @Autowired
    public TransactionService (TransactionRepository transactionRepository, UserService userService, WalletService walletService){
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.walletService = walletService;
    }

    public Transaction getTransaction(int id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public List<Transaction> getGamblerTransactions(int gambler_id) throws Exception {
        Gambler gambler = userService.getGamblerById(gambler_id);
        if(gambler == null)
            throw new Exception("Gambler does not exist!");
        return transactionRepository.findAllByGambler(gambler);
    }

    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    public Transaction addTransaction(Transaction t) throws Exception {

        if(t == null)
            throw new Exception("Null Transaction!");

        Gambler gambler = t.getGambler();
        if(gambler != null && !userService.gamblerExistsById(gambler.getId()))
            throw new Exception("Cannot register a transaction to a non existent gambler!");

        if (t.getValue() < 0)
            throw new Exception("Cannot perform a transaction with a negative value!");

        Wallet wallet = t.getWallet();
        if(wallet != null) {
            if(!walletService.walletExistsById(wallet.getId()))
                throw new Exception("Invalid wallet!");

            Float balance_after_mov = t.getBalance_after_mov();
            if(balance_after_mov != null && balance_after_mov < 0)
                throw new Exception("Balance cannot be negative!");
        }

        //Set date
        t.setDate(LocalDateTime.now());

        return transactionRepository.save(t);
    }

    public Transaction deposit(int wallet_id, float valueToDeposit) throws Exception {
        Integer gambler_id = walletService.findGamblerIdByWalletId(wallet_id);
        if(gambler_id == null)
            throw new Exception("A gambler does not exist for the given wallet! This should not be happening...");
        Wallet wallet = walletService.addToBalance(wallet_id, valueToDeposit);

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setValue(valueToDeposit);
        transaction.setDescription("Deposit");
        transaction.setBalance_after_mov(wallet.getBalance());
        Gambler gambler = new Gambler();
        gambler.setId(gambler_id);
        transaction.setGambler(gambler);

        return addTransaction(transaction);
    }

    public Transaction withdraw(int wallet_id, float valueToWithdraw) throws Exception {
        Integer gambler_id = walletService.findGamblerIdByWalletId(wallet_id);
        if(gambler_id == null)
            throw new Exception("A gambler does not exist for the given wallet! This should not be happening...");
        Wallet wallet = walletService.removeFromBalance(wallet_id, valueToWithdraw);

        Transaction transaction = new Transaction();
        transaction.setWallet(wallet);
        transaction.setValue(valueToWithdraw);
        transaction.setDescription("Withdraw");
        transaction.setBalance_after_mov(wallet.getBalance());
        Gambler gambler = new Gambler();
        gambler.setId(gambler_id);
        transaction.setGambler(gambler);

        return addTransaction(transaction);
    }

    public void removeTransaction(int id) throws Exception {
        if(!transactionRepository.existsById(id))
            throw new Exception("Transaction needs to exist, to be removed!");
        transactionRepository.deleteById(id);
    }
}

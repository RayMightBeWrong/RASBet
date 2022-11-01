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
    private final GamblerRepository gamblerRepository;
    private final WalletRepository walletRepository;
    @Autowired
    public TransactionService (TransactionRepository transactionRepository, GamblerRepository gamblerRepository, WalletRepository walletRepository){
        this.transactionRepository = transactionRepository;
        this.gamblerRepository = gamblerRepository;
        this.walletRepository = walletRepository;
    }

    public Transaction getTransaction(int id) {
        return transactionRepository.findById(id).orElse(null);
    }

    public Transaction addTransaction(Transaction t) throws Exception {

        if(t == null)
            throw new Exception("Null Transaction!");

        Gambler gambler = t.getGambler();
        if(gambler != null && !gamblerRepository.existsById(gambler.getId()))
            throw new Exception("Cannot register a transaction to a non existent gambler!");

        if (t.getValue() < 0)
            throw new Exception("Cannot perform a transaction with a negative value!");

        Wallet wallet = t.getWallet();
        if(wallet != null) {
            if(!walletRepository.existsById(wallet.getId()))
                throw new Exception("Invalid wallet!");

            Float balance_after_mov = t.getBalance_after_mov();
            if(balance_after_mov != null && balance_after_mov < 0)
                throw new Exception("Balance cannot be negative!");
        }

        //Set date
        t.setDate(LocalDateTime.now());

        return transactionRepository.save(t);
    }

    public List<Transaction> getGamblerTransactions(int gambler_id) throws Exception {
        Gambler gambler = gamblerRepository.findById(gambler_id).orElse(null);
        if(gambler == null)
            throw new Exception("Gambler does not exist!");
        return transactionRepository.findAllByGambler(gambler);
    }

    public void removeTransaction(int id) throws Exception {
        if(!transactionRepository.existsById(id))
            throw new Exception("Transaction needs to exist, to be removed!");
        transactionRepository.deleteById(id);
    }

    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }
}

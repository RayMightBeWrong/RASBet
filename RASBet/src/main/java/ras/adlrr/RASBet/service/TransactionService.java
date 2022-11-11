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

    /**
     * Checks for the existence of a transaction with the given id. If the transaction exists, returns it.
     * @param id Identification of the transaction.
     * @return transaction if it exists, or null.
     */
    public Transaction getTransaction(int id) {
        return transactionRepository.findById(id).orElse(null);
    }

    /**
     * @param gambler_id Identification of the gambler that made the transactions.
     * @return list of transactions of a gambler present in the repository.
     */
    public List<Transaction> getGamblerTransactions(int gambler_id) throws Exception {
        Gambler gambler = userService.getGamblerById(gambler_id);
        if(gambler == null)
            throw new Exception("Gambler does not exist!");
        return transactionRepository.findAllByGambler(gambler);
    }

    /**
     * @return list of transactions present in the repository.
     */
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Adds a transaction to the repository.
     * @param t Transaction to be persisted.
     * @return transaction updated by the repository.
     * @throws Exception If any of the attributes does not meet the requirements an Exception is thrown indicating the error.
     */
    public Transaction addTransaction(Transaction t) throws Exception {
        //Cannot persist a null transaction
        if(t == null)
            throw new Exception("Null Transaction!");

        //Transaction must have a valid gambler associated
        Gambler gambler = t.getGambler();
        if(gambler == null || !userService.gamblerExistsById(gambler.getId()))
            throw new Exception("Cannot register a transaction to a non existent gambler!");

        //Transaction must have a valid coin associated
        Coin coin = t.getCoin();
        if(coin == null || !walletService.coinExistsById(coin.getId()))
            throw new Exception("Cannot register a transaction with a non existent coin!");

        //Transaction must have a valid value associated
        if (t.getValue() < 0)
            throw new Exception("Cannot perform a transaction with a negative value!");

        //Transaction if related to a wallet, then the wallet must be valid, and the balance of the wallet after the transaction must not be negative
        Wallet wallet = t.getWallet();
        if(wallet != null) {
            if(!walletService.walletExistsById(wallet.getId()))
                throw new Exception("Invalid wallet!");

            Float balance_after_mov = t.getBalance_after_mov();
            if(balance_after_mov != null && balance_after_mov < 0)
                throw new Exception("Balance cannot be negative!");
        }

        //Sets the date of the transaction to the current date
        t.setDate(LocalDateTime.now());

        return transactionRepository.save(t);
    }

    /**
     * Perform a deposit transaction.
     * @param wallet_id identification of the wallet that is the aim of the deposit.
     * @param valueToDeposit value to deposit.
     * @return transaction persisted in the repository.
     * @throws Exception if the value is negative.
     */
    public Transaction deposit(int wallet_id, float valueToDeposit) throws Exception {
        Wallet wallet = walletService.addToBalance(wallet_id, valueToDeposit);

        Transaction transaction = new Transaction();
        Coin coin = wallet.getCoin();
        transaction.setCoin(coin);
        transaction.setWallet(wallet);
        transaction.setValue(valueToDeposit);
        transaction.setDescription("Deposit");
        transaction.setBalance_after_mov(wallet.getBalance());
        transaction.setGambler(wallet.getGambler());

        return addTransaction(transaction);
    }

    /**
     * Perform a withdrawal transaction.
     * @param wallet_id identification of the wallet that is the aim of the withdrawal.
     * @param valueToWithdraw value to withdraw.
     * @return transaction persisted in the repository.
     * @throws Exception if the value is negative.
     */
    public Transaction withdraw(int wallet_id, float valueToWithdraw) throws Exception {
        Wallet wallet = walletService.removeFromBalance(wallet_id, valueToWithdraw);

        Transaction transaction = new Transaction();
        transaction.setCoin(wallet.getCoin());
        transaction.setWallet(wallet);
        transaction.setValue(valueToWithdraw);
        transaction.setDescription("Withdraw");
        transaction.setBalance_after_mov(wallet.getBalance());
        transaction.setGambler(wallet.getGambler());

        return addTransaction(transaction);
    }

    /**
     * If a transaction with the given identification exists, removes it from the repository.
     * @param id identification of the transaction.
     * @throws Exception If the transaction does not exist.
     */
    public void removeTransaction(int id) throws Exception {
        if(!transactionRepository.existsById(id))
            throw new Exception("Transaction needs to exist, to be removed!");
        transactionRepository.deleteById(id);
    }
}
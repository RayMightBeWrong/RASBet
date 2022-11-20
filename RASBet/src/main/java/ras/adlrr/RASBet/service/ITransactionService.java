package ras.adlrr.RASBet.service;

import java.util.List;

import ras.adlrr.RASBet.model.Transaction;

public interface ITransactionService {
    public Transaction getTransaction(int id);

    public List<Transaction> getGamblerTransactions(int gambler_id) throws Exception;

    public List<Transaction> getTransactions();

    public Transaction addTransaction(Transaction t) throws Exception;

    public Transaction deposit(int wallet_id, float valueToDeposit) throws Exception;

    public Transaction withdraw(int wallet_id, float valueToWithdraw) throws Exception;

    public void removeTransaction(int id) throws Exception;
}

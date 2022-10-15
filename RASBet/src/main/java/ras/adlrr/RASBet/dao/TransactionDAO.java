package ras.adlrr.RASBet.dao;

import ras.adlrr.RASBet.model.Transaction;
import java.util.List;

public interface TransactionDAO {
    Transaction getTransaction(int id);
    int addTransaction(Transaction t);
    List<Transaction> getUserTransactions(int userID);
}

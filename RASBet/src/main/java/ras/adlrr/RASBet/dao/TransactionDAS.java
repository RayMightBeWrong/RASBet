package ras.adlrr.RASBet.dao;

import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Transaction;
import java.util.List;

//TODO
@Repository("TransactionDAO")
public class TransactionDAS implements TransactionDAO{
    @Override
    public Transaction getTransaction(int id) {
        return null;
    }

    @Override
    public int addTransaction(Transaction t) {
        return 0;
    }

    @Override
    public List<Transaction> getUserTransactions(int userID) {
        return null;
    }
}

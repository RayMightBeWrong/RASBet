package ras.adlrr.RASBet.dao;

import jdk.jshell.spi.ExecutionControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Transaction;
import java.util.List;

//TODO
@Repository("TransactionDAO")
public class TransactionDataAcessService implements TransactionDAO{
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

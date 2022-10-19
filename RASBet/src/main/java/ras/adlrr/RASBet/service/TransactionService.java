package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.TransactionDAO;
import ras.adlrr.RASBet.model.Transaction;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionDAO transactionDAO;

    @Autowired
    public TransactionService(@Qualifier("TransactionDAO") TransactionDAO t){
        this.transactionDAO = t;
    }

    public Transaction getTransaction(int id) {
        return transactionDAO.getTransaction(id);
    }

    public int addTransaction(Transaction t) {
        return transactionDAO.addTransaction(t);
    }

    public List<Transaction> getUserTransactions(int userID) {
        return transactionDAO.getUserTransactions(userID);
    }
}

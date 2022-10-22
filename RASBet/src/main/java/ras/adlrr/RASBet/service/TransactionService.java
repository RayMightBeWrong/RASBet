package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.TransactionRepository;
import ras.adlrr.RASBet.model.Transaction;

import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository tr;

    @Autowired
    public TransactionService (TransactionRepository tr){
        this.tr = tr;
    }

    public Transaction getTransaction(int id) {
        return tr.findById(id).orElse(null);
    }

    public int addTransaction(Transaction t) {
        tr.save(t);
        return 1;
    }

    public List<Transaction> getUserTransactions(int userID) {
        //return tr.findBy(User.class, userID);
        return null;
    }

    public int removeTransaction(int id) {
        tr.deleteById(id);
        return 1;
    }

    public List<Transaction> getTransactions() {
        return tr.findAll();
    }
}

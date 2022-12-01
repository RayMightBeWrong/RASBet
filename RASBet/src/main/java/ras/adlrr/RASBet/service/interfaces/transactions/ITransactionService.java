package ras.adlrr.RASBet.service.interfaces.transactions;

import java.util.List;

import org.springframework.data.domain.Sort;
import ras.adlrr.RASBet.model.Transaction;

public interface ITransactionService {
    public Transaction getTransaction(int id);

    List<Transaction> getGamblerTransactions(int gambler_id, Sort.Direction direction);

    public List<Transaction> getGamblerTransactions(int gambler_id);

    public List<Transaction> getTransactions();

    public Transaction addTransaction(Transaction t) throws Exception;

    public void removeTransaction(int id) throws Exception;
}

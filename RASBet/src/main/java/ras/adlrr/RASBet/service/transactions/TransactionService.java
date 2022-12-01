package ras.adlrr.RASBet.service.transactions;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.TransactionRepository;
import ras.adlrr.RASBet.model.*;
import ras.adlrr.RASBet.service.interfaces.transactions.ITransactionService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service("transactionService")
@RequiredArgsConstructor
public class TransactionService implements ITransactionService {
    private final TransactionRepository transactionRepository;

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
     * @param direction Defines the order of the transactions, by date. If 'null', no order is imposed.
     * @return list of transactions of a gambler present in the repository.
     */
    public List<Transaction> getGamblerTransactions(int gambler_id, Sort.Direction direction) {
        //TODO
        //if(!gamblerService.gamblerExistsById(gambler_id))
        //    throw new Exception("Gambler does not exist!");
        return direction == null ? transactionRepository.findAllByGamblerId(gambler_id) :
                transactionRepository.findAllByGamblerId(gambler_id, Sort.by(direction, "date"));
    }

    /**
     * @param gambler_id Identification of the gambler that made the transactions.
     * @return list of transactions of a gambler present in the repository.
     */
    public List<Transaction> getGamblerTransactions(int gambler_id) {
        return getGamblerTransactions(gambler_id, null);
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
        if(gambler == null)
            throw new Exception("Cannot register a transaction with a null gambler!");

        //Transaction must have a valid coin associated
        Coin coin = t.getCoin();
        if(coin == null)
            throw new Exception("Cannot register a transaction with a null coin!");

        //Transaction if related to a wallet, then the wallet must be valid, and the balance of the wallet after the transaction must not be negative
        Wallet wallet = t.getWallet();
        if(wallet != null) {
            float balance_after_mov = wallet.getBalance();
            if(balance_after_mov < 0)
                throw new Exception("Balance, of a wallet, after a transaction cannot be negative!");
            t.setBalance_after_mov(balance_after_mov);
        }

        //Sets the date of the transaction to the current date
        t.setDate(LocalDateTime.now(ZoneId.of("UTC+00:00")));

        return transactionRepository.save(t);
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
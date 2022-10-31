package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.GamblerRepository;
import ras.adlrr.RASBet.dao.TransactionRepository;
import ras.adlrr.RASBet.dao.WalletRepository;
import ras.adlrr.RASBet.model.Gambler;
import ras.adlrr.RASBet.model.Transaction;
import ras.adlrr.RASBet.model.Wallet;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository tr;
    private final GamblerRepository gr;
    private final WalletRepository wr;
    @Autowired
    public TransactionService (TransactionRepository tr, GamblerRepository gr, WalletRepository wr){
        this.tr = tr; this.gr = gr; this.wr = wr;
    }

    public Transaction getTransaction(int id) {
        return tr.findById(id).orElse(null);
    }

    public Transaction addTransaction(Transaction t) {
        try {
            Gambler g = gr.findById(t.getGambler().getID()).orElse(null);
            assert g != null;
            t.setGambler(g);
            g.addTransaction(t);
            t = tr.save(t);

            Integer wallet_id = t.getWallet_id();
            if(wallet_id != null) {
                Wallet w = wr.findById(wallet_id).orElse(null);
                assert w != null;
                w.addTransaction(t);
                wr.save(w);
            }

            return t;
        }catch (Exception e) { return null; }
    }

    public List<Transaction> getUserTransactions(int userID) {
        Gambler gambler = new Gambler(); gambler.setID(userID);
        return tr.findAllByGambler(gambler);
    }

    public int removeTransaction(int id) {
        try {
            tr.deleteById(id);
            return 1;
        }catch (Exception e) { return 0; }
    }

    public List<Transaction> getTransactions() {
        return tr.findAll();
    }
}

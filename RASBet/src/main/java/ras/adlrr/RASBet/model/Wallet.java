package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wallet")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int ID;
    private float balance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coin_id", nullable = false)
    private Coin coin;

    @OneToMany(/*mappedBy = "wallet",*/ cascade = CascadeType.ALL)
    @JoinColumn(name = "wallet_id")
    private List<Transaction> transactions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gambler_id", nullable = false)
    private Gambler gambler;

    public Wallet() {}
    public Wallet(@JsonProperty("id") int iD, @JsonProperty("balance") float balance) {
        ID = iD;
        this.balance = balance;
    }
    public Wallet(Wallet w) {
        this.ID = w.ID;
        this.balance = w.balance;
        this.gambler = w.getGambler();
        this.transactions = w.getTransactions();
        this.coin = w.getCoin();
    }

    public void setID(int iD) {
        ID = iD;
    }
    public int getID() {
        return ID;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }
    public float getBalance() {
        return balance;
    }

    public Coin getCoin() {
        return coin;
    }
    public void setCoin(Coin coin) {
        this.coin = coin;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Gambler getGambler() {
        return gambler;
    }

    public void setGambler(Gambler gambler) {
        this.gambler = gambler;
    }

    public Wallet clone(){
        return new Wallet(this);
    }

    public int changeBalance(float change){
        if (this.balance + change > 0){
            this.balance += change;
        }
        else this.balance = 0;
        return 1;
    }

    public void addTransaction(Transaction t){
        if(transactions == null) transactions = new ArrayList<>();
        transactions.add(t);
    }
}

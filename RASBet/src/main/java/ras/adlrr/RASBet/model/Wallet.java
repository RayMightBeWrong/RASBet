package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
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
    @JoinColumn(name = "coin_id")
    private Coin coin;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "wallet")
    private List<Transaction> transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gambler_id")
    private Gambler gambler;

    public Wallet() {}
    public Wallet(@JsonProperty("id") int iD, @JsonProperty("balance") float balance) {
        ID = iD;
        this.balance = balance;
    }


    public void setID(int iD) {
        ID = iD;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getID() {
        return ID;
    }

    public float getBalance() {
        return balance;
    }


    public Wallet clone(){
        return new Wallet(this.ID, this.balance);
    }

    public int changeBalance(float change){
        if (this.balance + change > 0){
            this.balance += change;
        }
        else return 0;
        return 1;
    }

    public void setCoin(Coin coin) {
        this.coin = coin.clone();
    }
}

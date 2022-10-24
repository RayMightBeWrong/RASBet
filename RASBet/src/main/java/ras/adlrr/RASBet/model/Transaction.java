package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    //@JoinColumn(name = "id", referencedColumnName = "id")
    private Bet bet;
    private double balance_after_mov;
    private String description;
    private float value;
    private LocalDateTime date;
    //private int wallet_id;


   // É suposto ser OneToOne you dumbass



    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;
    private int gambler_id;

    public Transaction(){}

    public Transaction(@JsonProperty("id") int id, @JsonProperty("balance_after_mov") double balance_after_mov, @JsonProperty("description") String description, @JsonProperty("value") float value, @JsonProperty("date") LocalDateTime date, /*@JsonProperty("wallet_id") int wallet_id,*/ @JsonProperty("gambler_id") int gambler_id) {
        this.id = id;
        this.balance_after_mov = balance_after_mov;
        this.description = description;
        this.value = value;
        this.date = date;
        //this.wallet_id = wallet_id;
        this.gambler_id = gambler_id;
    }

    public Transaction(Transaction transaction) {
        this.id = transaction.id;
        this.balance_after_mov = transaction.balance_after_mov;
        this.description = transaction.description;
        this.value = transaction.value;
        this.date = transaction.date;
        //this.wallet_id = transaction.wallet_id;
        this.gambler_id = transaction.gambler_id;
    }

    public int getId() {
        return id;
    }

    public double getBalance_after_mov() {
        return balance_after_mov;
    }

    public String getDescription() {
        return description;
    }

    public float getValue() {
        return value;
    }

    public LocalDateTime getDate() {
        return date;
    }

    /*public int getWallet_id() {
        return wallet_id;
    }*/

    public int getGambler_id() {
        return gambler_id;
    }



}

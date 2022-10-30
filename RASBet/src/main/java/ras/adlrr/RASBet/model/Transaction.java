package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import java.lang.annotation.Native;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private int id;

    @Transient
    private Integer wallet_id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "gambler_id")
    @JsonIgnore
    private Gambler gambler;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    private Bet bet;

    private double balance_after_mov;

    private String description;

    private float value;

    private LocalDateTime date;

    public Transaction(){}

    public Transaction(@JsonProperty("id") int id, @JsonProperty("balance_after_mov") double balance_after_mov,
                       @JsonProperty("description") String description, @JsonProperty("value") float value,
                       @JsonProperty("date") LocalDateTime date, @JsonProperty("wallet_id") Integer wallet_id, @JsonProperty("gambler_id") @NonNull int gambler_id) {
        this.id = id;
        this.balance_after_mov = balance_after_mov;
        this.description = description;
        this.value = value;
        this.date = date;
        this.gambler = new Gambler(); gambler.setID(gambler_id);
        this.wallet_id = wallet_id;
    }

    public Transaction(Transaction transaction) {
        this.id = transaction.id;
        this.balance_after_mov = transaction.balance_after_mov;
        this.description = transaction.description;
        this.value = transaction.value;
        this.date = transaction.date;
        this.bet = transaction.getBet();
        this.gambler = transaction.getGambler();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bet getBet() {
        return bet;
    }

    public void setBet(Bet bet) {
        this.bet = bet;
    }


    public double getBalance_after_mov() {
        return balance_after_mov;
    }

    public void setBalance_after_mov(double balance_after_mov) {
        this.balance_after_mov = balance_after_mov;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Integer getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(Integer wallet_id) {
        this.wallet_id = wallet_id;
    }

    public Gambler getGambler() {
        return gambler;
    }

    public void setGambler(Gambler gambler) {
        this.gambler = gambler;
    }
}

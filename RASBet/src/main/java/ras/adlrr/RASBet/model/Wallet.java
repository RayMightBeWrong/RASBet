package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wallet")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Wallet {
    //TODO - Logica precisa de verificar os campos obrigatorios
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "coin_id", updatable = false, nullable = false)
    @JsonIncludeProperties({"id"})
    private Coin coin;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gambler_id", updatable = false, nullable = false)
    @JsonIncludeProperties({"id"})
    private Gambler gambler;

    @OneToMany(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> transactions;

    private float balance;

    public Wallet(@JsonProperty("coin_id") int coin_id, @JsonProperty("gambler_id") int gambler_id) {
        coin = new Coin(); coin.setId(coin_id);
        gambler = new Gambler(); gambler.setId(gambler_id);
        balance = 0;
    }

    // Additional Methods

    public boolean changeBalance(float change){
        if(balance + change < 0) return false;
        balance += change;
        return true;
    }

    public void addTransaction(Transaction t){
        if(transactions == null) transactions = new ArrayList<>();
        transactions.add(t);
    }

    @Override
    public String toString() {
        return "Wallet{" +
                "id=" + id +
                ", coin=" + coin +
                ", gambler=" + gambler +
                ", transactions=" + transactions +
                ", balance=" + balance +
                '}';
    }
}
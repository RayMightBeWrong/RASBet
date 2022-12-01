package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "gambler_id", updatable = false, nullable = false)
    @JsonIncludeProperties("id")
    private Gambler gambler = null;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    @JoinColumn(insertable = false, updatable = false)
    @JsonIncludeProperties("id")
    @JsonIgnore
    private Bet bet = null;

    @ManyToOne(optional = true)
    @JoinColumn(name = "wallet_id", updatable = false, nullable = true)
    @JsonIncludeProperties("id")
    private Wallet wallet = null;

    @ManyToOne(optional = false)
    @JoinColumn(name = "coin_id", updatable = false, nullable = false)
    private Coin coin = null;
    private float value;
    private Float balance_after_mov;
    private String description;
    private LocalDateTime date = null;

    public Transaction(@JsonProperty("gambler_id") int gambler_id, @JsonProperty("wallet_id") Integer wallet_id,
                       @JsonProperty("balance_after_mov") Float balance_after_mov, @JsonProperty("description") String description,
                       @JsonProperty("value") float value, @JsonProperty("coin_id") String coin_id){
        this.gambler = new Gambler(); gambler.setId(gambler_id);
        if(coin_id != null) { this.coin = new Coin(); coin.setId(coin_id); }
        if(wallet_id != null) { this.wallet = new Wallet(); this.wallet.setId(wallet_id); }
        this.balance_after_mov = balance_after_mov;
        this.description = description;
        this.value = value;
    }
}
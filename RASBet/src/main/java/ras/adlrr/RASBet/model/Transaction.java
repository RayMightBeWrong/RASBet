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
    private Gambler gambler;

    @OneToOne(mappedBy = "transaction", cascade = CascadeType.ALL)
    @JoinColumn(insertable = false, updatable = false)
    @JsonIncludeProperties("id")
    @JsonIgnore
    private Bet bet;

    @ManyToOne(optional = true)
    @JoinColumn(name = "wallet_id", updatable = false, nullable = true)
    @JsonIncludeProperties("id")
    private Wallet wallet = null;

    private Float balance_after_mov;
    private String description;
    private float value;
    private LocalDateTime date;

    public Transaction(@JsonProperty("gambler_id") int gambler_id, @JsonProperty("wallet_id") Integer wallet_id,
                       @JsonProperty("balance_after_mov") Float balance_after_mov, @JsonProperty("description") String description,
                       @JsonProperty("value") float value, @JsonProperty("date") LocalDateTime date){
        this.gambler = new Gambler(); gambler.setId(gambler_id);
        if(wallet_id != null) { this.wallet = new Wallet(); this.wallet.setId(wallet_id);}
        this.balance_after_mov = balance_after_mov;
        this.description = description;
        this.value = value;
        this.date = date;
    }
}

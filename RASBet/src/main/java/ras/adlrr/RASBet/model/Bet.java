package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bets")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"id","state","game_choices","transaction"})
public class Bet {
    @Id
    @Column(name = "id")
    @JsonProperty("id")
    private int id;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "id", updatable = false)
    @MapsId
    @JsonIncludeProperties("id")
    private Transaction transaction;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "bet_id", nullable = false)
    private List<GameChoice> gameChoices;

    @JsonProperty("state")
    private int state = STATE_OPEN;

    public static final int STATE_OPEN = 1;
    public static final int STATE_CLOSED = 2;
    public static final int STATE_CANCELED = 3;

    public Bet(@JsonProperty("gambler_id") int gambler_id, @JsonProperty("wallet_id") Integer wallet_id,
               @JsonProperty("value") float value,
               @JsonProperty("game_choices") List<GameChoice> gameChoices){
        transaction = new Transaction();

        Gambler gambler = new Gambler();
        gambler.setId(gambler_id);
        transaction.setGambler(gambler);

        if(wallet_id != null) {
            Wallet wallet = new Wallet();
            wallet.setId(wallet_id);
            transaction.setWallet(wallet);
        }

        transaction.setValue(value);

        this.gameChoices = gameChoices;
    }
}
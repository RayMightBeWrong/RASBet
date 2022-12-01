package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
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
    @JoinColumn(name = "id", updatable = false, insertable = false)
    @MapsId
    @JsonIncludeProperties({"id","value","date"})
    private Transaction transaction;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "bet_id", nullable = false)
    private List<GameChoice> gameChoices;

    private String coupon = null;

    @JsonProperty("state")
    private int state = STATE_PENDING;

    public static final int STATE_PENDING = 1;
    public static final int STATE_WON = 2;
    public static final int STATE_LOST = 3;
    public static final int STATE_CANCELED = 4;

    public Bet(@JsonProperty("gambler_id") int gambler_id, @JsonProperty("wallet_id") Integer wallet_id,
               @JsonProperty("value") float value, @JsonProperty("coin_id") String coin_id,
               @JsonProperty("coupon") String coupon,
               @JsonProperty("game_choices") List<GameChoice> gameChoices){
        transaction = new Transaction();

        Gambler gambler = new Gambler();
        gambler.setId(gambler_id);
        transaction.setGambler(gambler);

        if(coin_id != null){
            Coin coin = new Coin();
            coin.setId(coin_id);
            transaction.setCoin(coin);
        }

        if(wallet_id != null) {
            Wallet wallet = new Wallet();
            wallet.setId(wallet_id);
            transaction.setWallet(wallet);
        }

        transaction.setValue(value);

        this.coupon = coupon;
        this.gameChoices = gameChoices;
    }

    public void addGameChoice(GameChoice gc){
        if(gameChoices == null)
            gameChoices = new ArrayList<>();
        gameChoices.add(gc);
    }
}
package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

//TODO - Provavelmente é melhor esta classe ser independente
public class Bet extends Transaction{
    private int bet_id;
    private int state;
    private List<GameChoice> gameChoices;

    public Bet(@JsonProperty("transaction_id") int Transaction_id, @JsonProperty("balance_after_mov") double balance_after_mov, @JsonProperty("description") String description, @JsonProperty("value") float value, @JsonProperty("date") LocalDateTime date, @JsonProperty("wallet_id") int wallet_id, @JsonProperty("gambler_id") int gambler_id, @JsonProperty("bet_id") int bet_id, @JsonProperty("state") int state, @JsonProperty("gameChoices") List<GameChoice> gameChoices) {
        super(Transaction_id, balance_after_mov, description, value, date, wallet_id, gambler_id);
        this.bet_id = bet_id;
        this.state = state;
        this.gameChoices = gameChoices.stream().map(GameChoice::new).toList();
    }

    public Bet(Bet bet) {
        super(bet);
        this.bet_id = bet.bet_id;
        this.state = bet.state;
        this.gameChoices = bet.getGameChoices();
    }

    public Bet(Transaction t, int bet_id, int state, List<GameChoice> gameChoices) {
        super(t);
        this.bet_id = bet_id;
        this.state = state;
        this.gameChoices = gameChoices.stream().map(GameChoice::new).toList();;
    }


    public int getId() {
        return bet_id;
    }

    public int getState() {
        return state;
    }

    public List<GameChoice> getGameChoices(){
        return gameChoices.stream().map(GameChoice::new).toList();
    }
}

package ras.adlrr.RASBet.model;

import java.time.LocalDateTime;

public class Bet extends Transaction{
    private int bet_id;
    private boolean is_multiple;
    private int state;
    private int game_id;

    public Bet(int Transaction_id, double balance_after_mov, String description, float value, LocalDateTime date, int wallet_id, int gambler_id, int bet_id, boolean is_multiple, int state, int game_id) {
        super(Transaction_id, balance_after_mov, description, value, date, wallet_id, gambler_id);
        this.bet_id = bet_id;
        this.is_multiple = is_multiple;
        this.state = state;
        this.game_id = game_id;
    }

    public int getId() {
        return bet_id;
    }

    public boolean is_multiple() {
        return is_multiple;
    }

    public int getState() {
        return state;
    }

    public int getGame_id() {
        return game_id;
    }
}

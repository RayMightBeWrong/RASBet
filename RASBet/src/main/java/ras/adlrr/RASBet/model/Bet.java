package ras.adlrr.RASBet.model;

public class Bet {
    private int id;
    private boolean is_multiple;
    private int state;
    private int game_id;

    public Bet(int id, boolean is_multiple, int state, int game_id) {
        this.id = id;
        this.is_multiple = is_multiple;
        this.state = state;
        this.game_id = game_id;
    }

    public int getId() {
        return id;
    }

    public boolean isIs_multiple() {
        return is_multiple;
    }

    public int getState() {
        return state;
    }

    public int getGame_id() {
        return game_id;
    }
}

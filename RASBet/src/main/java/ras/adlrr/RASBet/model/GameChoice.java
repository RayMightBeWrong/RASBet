package ras.adlrr.RASBet.model;

public class GameChoice {
    private int game_id;
    private String participant;

    public GameChoice(int game_id, String participant) {
        this.game_id = game_id;
        this.participant = participant;
    }

    public GameChoice(GameChoice g){
        this.game_id = g.game_id;
        this.participant = g.participant;
    }

    public int getGame_id() {
        return game_id;
    }

    public String getParticipant() {
        return participant;
    }
}

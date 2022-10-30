package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

@Entity
@Table(name = "game_choices")
public class GameChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    private int odd;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "participant_id")
    private Participant participant;

    //TODO - ver todas as funcoes

    public GameChoice() {
    }

    public GameChoice(@JsonProperty("game_id") int game_id, @JsonProperty("participant") Participant participant) {
        this.game = new Game(); game.setId(game_id);
        this.participant = participant;
    }

    public GameChoice(GameChoice g){
        //this.game_id = g.game_id;
        //this.participant = g.participant;
    }

    public int getGame_id() {
        return 0;
    }

    public String getParticipant() {
        return null;
    }
}

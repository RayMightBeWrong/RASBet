package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "bets")
public class Bet implements Serializable {
    @Id
    @Column(name = "id")
    private int id;

    @OneToOne(cascade = CascadeType.ALL, optional = false)
    @JoinColumn(name = "id")
    @MapsId
    private Transaction transaction;

    /*
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "bet_id", nullable = false)
    private List<GameChoice> gameChoices;
*/
    private int state;

    public Bet(){}

    public Bet(@JsonProperty("id") int id, @JsonProperty("state") int state, @JsonProperty("transaction") Transaction transaction, @JsonProperty("game_choices") List<GameChoice> gameChoices) {
        this.id = id;
        this.state = state;
        this.transaction = transaction;
        //this.gameChoices = gameChoices;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    /*
    public List<GameChoice> getGameChoices() {
        return gameChoices;
    }

    public void setGameChoices(List<GameChoice> gameChoices) {
        this.gameChoices = gameChoices;
    }*/

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

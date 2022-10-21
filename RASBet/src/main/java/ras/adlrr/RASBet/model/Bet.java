package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bets")
public class Bet implements Serializable {
    @Id
    @Column(name = "id")
    private int id;
    private int state;
    //private List<GameChoice> gameChoices;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    @MapsId
    private Transaction transaction;

    public Bet(){}

    public Bet(@JsonProperty("id") int id, @JsonProperty("state") int state) {
        this.id = id;
        this.state = state;
        //this.gameChoices = gameChoices.stream().map(GameChoice::new).toList();
    }

    public Bet(int state){
        this.state = state;
    }

    /*
    public Bet(@JsonProperty("state") int state, @JsonProperty("gameChoices") List<GameChoice> gameChoices) {
        this.state = state;
        this.gameChoices = gameChoices.stream().map(GameChoice::new).toList();
    }
    */


    public Bet(int state, List<GameChoice> gameChoices, Transaction transaction) {
        this.state = state;
        //this.gameChoices = gameChoices.stream().map(GameChoice::new).toList();
        this.transaction = new Transaction(transaction);
    }

    public Bet(Bet bet) {
        this.state = bet.state;
        //this.gameChoices = bet.getGameChoices();
        this.transaction = bet.getTransaction();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getState() {
        return state;
    }

    /*public List<GameChoice> getGameChoices(){
        return gameChoices.stream().map(GameChoice::new).toList();
    }*/

    public Transaction getTransaction() {
        return new Transaction(transaction);
    }

    public void setState(int state) {
        this.state = state;
    }

    /*public void setGameChoices(List<GameChoice> gameChoices) {
        this.gameChoices = gameChoices.stream().map(GameChoice::new).toList();
    }*/

    public void setTransaction(Transaction transaction) {
        this.transaction = new Transaction(transaction);
    }

    /*

    package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "bets")
public class Bet{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    private int state;
    private List<GameChoice> gameChoices;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Transaction transaction;

    public Bet(){}

    public Bet(@JsonProperty("bet_id") int id, @JsonProperty("state") int state, @JsonProperty("gameChoices") List<GameChoice> gameChoices) {
        this.id = id;
        this.state = state;
        this.gameChoices = gameChoices.stream().map(GameChoice::new).toList();
    }

    public Bet(int id, int state, List<GameChoice> gameChoices, Transaction transaction) {
        this.id = id;
        this.state = state;
        this.gameChoices = gameChoices.stream().map(GameChoice::new).toList();
        this.transaction = new Transaction(transaction);
    }

    public Bet(Bet bet) {
        this.id = bet.id;
        this.state = bet.state;
        this.gameChoices = bet.getGameChoices();
        this.transaction = bet.getTransaction();
    }


    public int getId() {
        return id;
    }

    public int getState() {
        return state;
    }

    public List<GameChoice> getGameChoices(){
        return gameChoices.stream().map(GameChoice::new).toList();
    }

    public Transaction getTransaction() {
        return new Transaction(transaction);
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setGameChoices(List<GameChoice> gameChoices) {
        this.gameChoices = gameChoices.stream().map(GameChoice::new).toList();
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = new Transaction(transaction);
    }
}


     */
}

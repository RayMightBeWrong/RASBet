package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "participants")
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(updatable = false)
    private String name;

    @Column(updatable = false)
    private float odd;

    @Column(updatable = false)
    private int score;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id")
    @JsonIgnore
    private Game game;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "participant")
    private List<GameChoice> gameChoices;

    public Participant(){}

    public Participant(@JsonProperty("name") String name, @JsonProperty("odd") float odd, @JsonProperty("score") int score) {
        this.name = name;
        this.odd = odd;
        this.score = score;
    }

    public Participant(Participant p){
        this.name = p.name;
        this.odd = p.odd;
        this.score = p.score;
    }

    public int getId(){
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(){
        this.name = name;
    }

    public float getOdd() {
        return odd;
    }

    public void setOdd(float odd){
        this.odd = odd;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score){
        this.score = score;
    }

    public Game getGame(){
        return game;
    }

    public void setGame(Game game){
        this.game = game;
    }
}

package ras.adlrr.RASBet.model;

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
    private String name;
    private float odd;
    private int score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "game_id")
    private Game game;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "participant")
    private List<GameChoice> gameChoices;

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

    public String getName() {
        return name;
    }

    public float getOdd() {
        return odd;
    }

    public int getScore() {
        return score;
    }
}

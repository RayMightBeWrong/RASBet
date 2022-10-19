package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Participant {
    private int name;
    private float odd;
    private int score;

    public Participant(@JsonProperty("name") int name, @JsonProperty("odd") float odd, @JsonProperty("score") int score) {
        this.name = name;
        this.odd = odd;
        this.score = score;
    }

    public Participant(Participant p){
        this.name = p.name;
        this.odd = p.odd;
        this.score = p.score;
    }

    public int getName() {
        return name;
    }

    public float getOdd() {
        return odd;
    }

    public int getScore() {
        return score;
    }
}

package ras.adlrr.RASBet.model;

public class Participant {
    private int name;
    private float odd;
    private int score;

    public Participant(int name, float odd, int score) {
        this.name = name;
        this.odd = odd;
        this.score = score;
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

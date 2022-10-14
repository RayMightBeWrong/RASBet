package ras.adlrr.RASBet.model;

public class Coin {
    private int ID;
    private String name;
    private float ratio_EUR;

    public Coin(int ID, String name, float ratio_EUR){
        this.ID = ID;
        this.name = name;
        this.ratio_EUR = ratio_EUR;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setRatio_EUR(float ratio_EUR) {
        this.ratio_EUR = ratio_EUR;
    }

    public int getID(){
        return this.ID;
    }

    public String getName(){
        return this.name;
    }

    public float getRatio_EUR(){
        return this.ratio_EUR;
    }

    public Coin clone(){
        return new Coin(this.ID, this.name, this.ratio_EUR);
    }
}

package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Wallet {
    private int ID;
    private float balance;
    private int coinID;
    
    public Wallet(@JsonProperty("id") int iD, @JsonProperty("balance") float balance, @JsonProperty("coin") int coinID) {
        ID = iD;
        this.balance = balance;
        this.coinID = coinID;
    }

    public void setID(int iD) {
        ID = iD;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public void setCoinID(int coinID) {
        this.coinID = coinID;
    }

    public int getID() {
        return ID;
    }

    public float getBalance() {
        return balance;
    }

    public int getCoinID() {
        return coinID;
    }

    public Wallet clone(){
        return new Wallet(this.ID, this.balance, this.coinID);
    }
}

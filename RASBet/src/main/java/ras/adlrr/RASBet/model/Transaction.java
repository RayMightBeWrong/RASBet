package ras.adlrr.RASBet.model;

import java.time.LocalDateTime;

public class Transaction {
    private int id;
    private double balance_after_mov;
    private String description;
    private float value;
    private LocalDateTime date;
    private int wallet_id;
    private int gambler_id;

    public Transaction(int id, double balance_after_mov, String description, float value, LocalDateTime date, int wallet_id, int gambler_id) {
        this.id = id;
        this.balance_after_mov = balance_after_mov;
        this.description = description;
        this.value = value;
        this.date = date;
        this.wallet_id = wallet_id;
        this.gambler_id = gambler_id;
    }

    public int getId() {
        return id;
    }

    public double getBalance_after_mov() {
        return balance_after_mov;
    }

    public String getDescription() {
        return description;
    }

    public float getValue() {
        return value;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public int getGambler_id() {
        return gambler_id;
    }
}

package ras.adlrr.RASBet.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Gambler extends User{
    private String CC; // cartão de cidadão;
    private String nationality;
    private int NIF;
    // TODO: talvez só se põe depois com a base de dados
    private LocalDate date_of_birth;
    private String email;
    private String postal_code;
    private String city;
    private String address;
    private String ocupation;
    private int phoneNumber;
    private Map<Integer, Wallet> wallets;

    public Gambler(int ID, String name, String CC, String nationality, int NIF, String ocupation, int phoneNumber,
                    LocalDate date_of_birth, String email, String postal_code, String address){
        super(ID, name);
        this.CC = CC;
        this.NIF = NIF;
        this.date_of_birth = date_of_birth;
        this.email = email;
        this.postal_code = postal_code;
        this.address = address;
        this.ocupation = ocupation;
        this.phoneNumber = phoneNumber;
        this.wallets = new HashMap<>();
    }

    public void setCC(String cC) {
        this.CC = cC;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setNIF(int nIF) {
        this.NIF = nIF;
    }

    public void setDate_of_birth(LocalDate date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOcupation(String ocupation) {
        this.ocupation = ocupation;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setWallets(Map<Integer, Wallet> wallets) {
        Map<Integer, Wallet> copycat = new HashMap<>();
        for (Map.Entry<Integer, Wallet> entry : wallets.entrySet()) {
            copycat.put(entry.getKey(), entry.getValue().clone());
        }
        this.wallets = copycat;
    }

    public String getCC() {
        return this.CC;
    }

    public String getNationality() {
        return this.nationality;
    }

    public int getNIF() {
        return this.NIF;
    }

    public LocalDate getDate_of_birth() {
        return this.date_of_birth;
    }

    public String getEmail() {
        return this.email;
    }

    public String getPostal_code() {
        return this.postal_code;
    }

    public String getCity() {
        return this.city;
    }

    public String getAddress() {
        return this.address;
    }

    public Map<Integer, Wallet> getWallets() {
        Map<Integer, Wallet> copycat = new HashMap<>();
        for (Map.Entry<Integer, Wallet> entry : this.wallets.entrySet()) {
            copycat.put(entry.getKey(), entry.getValue().clone());
        }
        return copycat;
    }

    public User clone(){
        return new Gambler(NIF, city, CC, nationality, NIF, ocupation, phoneNumber, date_of_birth, email, postal_code, address);
    }
}

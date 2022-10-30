package ras.adlrr.RASBet.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.yaml.snakeyaml.events.Event;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Gambler extends User{
    private String CC; // cartão de cidadão;
    private String nationality;
    private int NIF;
    private LocalDate date_of_birth;

    private String postal_code;
    private String city;
    private String address;
    private String occupation;
    private int phoneNumber;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gambler")
    private List<Wallet> wallets;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gambler")
    private List<Transaction> transactions;

    public Gambler(){}

    @JsonCreator
    public Gambler(@JsonProperty("id") int ID, @JsonProperty("name") String name, @JsonProperty("password") String password, @JsonProperty("cc") String CC, @JsonProperty("nationality") String nationality, @JsonProperty("nif") int NIF, @JsonProperty("occupation") String occupation, @JsonProperty("phone_numer") int phoneNumber,
                   @JsonProperty("date_of_birth") LocalDate date_of_birth, @JsonProperty("email") String email, @JsonProperty("postal_code") String postal_code, @JsonProperty("address") String address){
        super(ID, name, password,email);
        this.CC = CC;
        this.nationality = nationality;
        this.NIF = NIF;
        this.date_of_birth = date_of_birth;
        this.postal_code = postal_code;
        this.address = address;
        this.occupation = occupation;
        this.phoneNumber = phoneNumber;
        this.wallets = new ArrayList<>();
    }

    public Gambler(@JsonProperty("id") int ID, @JsonProperty("name") String name, @JsonProperty("password") String password, @JsonProperty("cc") String CC, @JsonProperty("nationality") String nationality, @JsonProperty("nif") int NIF, @JsonProperty("occupation") String occupation, @JsonProperty("phone_numer") int phoneNumber,
                   @JsonProperty("date_of_birth") LocalDate date_of_birth, @JsonProperty("email") String email, @JsonProperty("postal_code") String postal_code, @JsonProperty("address") String address, @JsonProperty("wallets") List<Wallet> wallets){
        super(ID, name, password,email);
        this.CC = CC;
        this.nationality = nationality;
        this.NIF = NIF;
        this.date_of_birth = date_of_birth;
        this.postal_code = postal_code;
        this.address = address;
        this.occupation = occupation;
        this.phoneNumber = phoneNumber;
        this.wallets = wallets.stream().map(Wallet::clone).toList();
    }

    public Gambler(int nif, String city, String cc, String nationality, String ocupation, int phoneNumber, LocalDate date_of_birth, String postal_code, String address) {
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

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setOcupation(String occupation) {
        this.occupation = occupation;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setWallets(List<Wallet> wallets) {
        this.wallets = wallets.stream().map(Wallet::clone).toList();;
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

    public String getPostal_code() {
        return this.postal_code;
    }

    public String getCity() {
        return this.city;
    }

    public String getAddress() {
        return this.address;
    }

    public List<Wallet> getWallets() {
        return wallets.stream().map(Wallet::clone).toList();
    }

    public Gambler clone(){
        return new Gambler(this.getID(), this.getName(), this.getPassword(), CC, nationality, NIF, occupation, phoneNumber, date_of_birth, this.getEmail(), postal_code, address, wallets) ;
    }
}

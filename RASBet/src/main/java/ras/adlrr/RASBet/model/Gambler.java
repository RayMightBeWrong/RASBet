package ras.adlrr.RASBet.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.NonNull;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Gambler extends User{
    @Column(name = "cc", nullable = false)
    private String cc; // cartão de cidadão;
    @Column(name = "nif", nullable = false)
    private Integer nif;
    @Column(name = "date_of_birth", nullable = false)
    private LocalDate date_of_birth; //format: "YYYY-MM-DD"
    private Integer phoneNumber;
    private String nationality;
    private String city;
    private String address;
    private String postal_code;
    private String occupation;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gambler", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Wallet> wallets;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "gambler", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> transactions;


    public Gambler(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("email") String email, @JsonProperty("password") String password,
                   @JsonProperty("cc") String cc, @JsonProperty("nif") Integer nif, @JsonProperty("date_of_birth") LocalDate date_of_birth,
                   @JsonProperty("phone_number") Integer phoneNumber, @JsonProperty("nationality") String nationality,
                   @JsonProperty("city") String city, @JsonProperty("address") String address, @JsonProperty("postal_code") String postal_code,
                   @JsonProperty("occupation") String occupation){
        super(id, name, password,email);
        this.cc = cc;
        this.nif = nif;
        this.date_of_birth = date_of_birth;
        this.phoneNumber = phoneNumber;
        this.nationality = nationality;
        this.city = city;
        this.address = address;
        this.postal_code = postal_code;
        this.occupation = occupation;
    }

    public Gambler(int id, String name, String email, String password,
                   String cc, int nif, LocalDate date_of_birth, int phoneNumber,
                   String nationality, String city, String address,
                   String postal_code, String occupation, List<Wallet> wallets,
                   List<Transaction> transactions){
        super(id, name, password,email);
        this.cc = cc;
        this.nif = nif;
        this.date_of_birth = date_of_birth;
        this.phoneNumber = phoneNumber;
        this.nationality = nationality;
        this.city = city;
        this.address = address;
        this.postal_code = postal_code;
        this.occupation = occupation;
        this.wallets = wallets;
        this.transactions = transactions;
    }

    // ----------- Additional Methods ------------

    public void addTransaction(Transaction t){
        if(transactions == null) transactions = new ArrayList<>();
        transactions.add(t);
    }

    public void addWallet(Wallet w){
        if(wallets == null) wallets = new ArrayList<>();
        wallets.add(w);
    }
}
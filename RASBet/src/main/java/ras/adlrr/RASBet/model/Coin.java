package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coin")
public class Coin {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private int ID;
    private String name;
    private float ratio_EUR;

    @OneToMany(
            mappedBy = "coin",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Wallet> wallets = new ArrayList<>();
    public Coin() {}
    public Coin(@JsonProperty("id") int ID, @JsonProperty("name") String name, @JsonProperty("ratio_EUR") float ratio_EUR){
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

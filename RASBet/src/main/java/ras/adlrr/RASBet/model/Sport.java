package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "sport")
public class Sport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id; //Unique identifier of the sport
    private String name;
    private int type; //Type of sport (E.g.: Collective and without draw / Non collective and with draw ...)

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sport")
    private List<Game> games;

    public Sport(){}

    public Sport(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("type") int type) {
        this.id = id;
        this.name =     name;
        this.type = type;
    }

    public Sport(@JsonProperty("name") String name, @JsonProperty("type") int type) {
        this.name = name;
        this.type = type;
    }

    public Sport(Sport sport){
        this.id = sport.id;
        this.name = sport.name;
        this.type = sport.type;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getType() {
        return type;
    }

    public void setId(int id){
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }
}

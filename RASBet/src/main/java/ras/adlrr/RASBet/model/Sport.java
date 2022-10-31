package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sport")
public class Sport {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id; //Unique identifier of the sport

    @Column(updatable = false)
    private String name;
    
    @Column(updatable = false)
    private int type; //Type of sport (E.g.: Collective and without draw / Non collective and with draw ...)

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sport", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Game> games;

    public Sport(){}

    public Sport(@JsonProperty("name") String name, @JsonProperty("type") int type) {
        this.name = name;
        this.type = type;
        this.games = new ArrayList<>();
    }

    public Sport(Sport sport){
        this.id = sport.id;
        this.name = sport.name;
        this.type = sport.type;
        this.games = sport.games;
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

    public List<Game> getGames(){
        return games;
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

    public void setGames(List<Game> games){
        this.games = games;
    }

    public int addGame(Game g){
        try{
            this.games.add(g);
            return 1;
        }
        catch(Exception e){
            return 0;
        }
    }
}

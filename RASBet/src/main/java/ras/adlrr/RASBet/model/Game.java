package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Game{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;

    @Column(updatable = false)
    private String extID;

    @Column(updatable = false)
    private LocalDateTime date;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "game_id")
    @JsonIgnore
    private List<Participant> participants;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id")
    @JsonIgnore
    private Sport sport;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    @JsonIgnore
    private List<GameChoice> gameChoice;
    
    @Column(updatable = false)
    private int state;

    public static final int SUSPENDED = 1;
    public static final int CLOSED = 2;
    public static final int OPEN = 3;

    public Game(@JsonProperty("id") int id, @JsonProperty("extID") String extID, @JsonProperty("date") LocalDateTime date, @JsonProperty("participants") List<Participant> participants, @JsonProperty("sportID") int sportID, @JsonProperty("state") int state) {
        this.id = id;
        this.extID = extID;
        this.date = date;
        this.sport = new Sport();
        this.sport.setId(sportID);
        this.participants = participants != null ? participants : new ArrayList<>();
        this.state = state;
    }

    public Game() {}

    public Game() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExtID() {
        return extID;
    }

    public void setExtID(String extID) {
        this.extID = extID;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public List<Participant> getParticipants() {
        return participants;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants;
    }

    public Sport getSport() {
        return sport;
    }

    public void setSport(Sport sport) {
        this.sport = sport;
    }

    public List<GameChoice> getGameChoice() {
        return gameChoice;
    }

    public void setGameChoice(List<GameChoice> gameChoice) {
        this.gameChoice = gameChoice;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int addParticipantToGame(Participant p){
        try{
            this.participants.add(p);
            return 1;
        }
        catch(Exception e){
            return 0;
        }
    }
}
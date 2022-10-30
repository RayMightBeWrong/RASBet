package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private int id;
    private String extID;
    private LocalDateTime date;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    private List<Participant> participants;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sport_id")
    private Sport sport;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game")
    private List<GameChoice> gameChoice;
    private int state;

    public static final int SUSPENDED = 1;
    public static final int CLOSED = 2;
    public static final int OPEN = 3;

    //TODO - ver o que fazer aqui, já n existe sportID, será q ao dar set no objeto Sport, funciona para insercao?
    public Game(@JsonProperty("id") int id, @JsonProperty("extID") String extID, @JsonProperty("date") LocalDateTime date, @JsonProperty("participants") List<Participant> participants, @JsonProperty("sportID") int sportID, @JsonProperty("state") int state) {
        this.id = id;
        this.extID = extID;
        this.date = date;
        this.participants = participants != null ? participants.stream().map(Participant::new).toList() : null;
        this.state = state;
    }

    public Game(Game g) {
        this.id = g.id;
        this.extID = g.extID;
        this.date = g.date;
        this.participants = g.getParticipants();
        this.state = g.state;
    }

    public Game() {

    }

    public int getId() {
        return id;
    }

    public String getExtID() {
        return extID;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<Participant> getParticipants() {
        return participants != null ? participants.stream().map(Participant::new).toList() : null;
    }

    public Sport getSport() {
        return new Sport(this.sport);
    }

    public int getState() {
        return state;
    }

    public void setParticipants(List<Participant> participants) {
        this.participants = participants != null ? participants.stream().map(Participant::new).toList() : null;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setExtID(String extID) {
        this.extID = extID;
    }

    public void setSport(Sport sport){
        this.sport = new Sport(sport);
    }
}

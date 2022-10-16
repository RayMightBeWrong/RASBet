package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private int id;
    private LocalDateTime date;
    private List<Participant> participants;
    private int sportID;
    private Float drawOdd; //Odd of draw - If 'null' indicates that the game does not allow draws
    private int state;


    public Game(@JsonProperty("id") int id, @JsonProperty("date") LocalDateTime date, @JsonProperty("participants") List<Participant> participants, @JsonProperty("sportID") int sportID, @JsonProperty("drawOdd") float drawOdd, @JsonProperty("state") int state) {
        this.id = id;
        this.date = date;
        this.participants = participants != null ? participants.stream().map(Participant::new).toList() : null;
        this.sportID = sportID;
        this.drawOdd = drawOdd;
        this.state = state;
    }

    public Game(Game g) {
        this.id = g.id;
        this.date = g.date;
        this.participants = g.getParticipants();
        this.sportID = g.sportID;
        this.drawOdd = g.drawOdd;
        this.state = g.state;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<Participant> getParticipants() {
        return participants != null ? participants.stream().map(Participant::new).toList() : null;
    }

    public int getSportID() {
        return this.sportID;
    }

    public Float getDrawOdd() {
        return drawOdd;
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
}

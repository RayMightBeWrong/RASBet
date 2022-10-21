package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

public class Game {
    private int id;
    private String extID;
    private LocalDateTime date;
    private List<Participant> participants;
    private int sportID;
    private int state;

    public static final int SUSPENDED = 1;
    public static final int CLOSED = 2;
    public static final int OPEN = 3;

    public Game(@JsonProperty("id") int id, @JsonProperty("extID") String extID, @JsonProperty("date") LocalDateTime date, @JsonProperty("participants") List<Participant> participants, @JsonProperty("sportID") int sportID, @JsonProperty("state") int state) {
        this.id = id;
        this.extID = extID;
        this.date = date;
        this.participants = participants != null ? participants.stream().map(Participant::new).toList() : null;
        this.sportID = sportID;
        this.state = state;
    }

    public Game(Game g) {
        this.id = g.id;
        this.extID = g.extID;
        this.date = g.date;
        this.participants = g.getParticipants();
        this.sportID = g.sportID;
        this.state = g.state;
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

    public int getSportID() {
        return this.sportID;
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
}

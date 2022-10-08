package ras.adlrr.RASBet.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Game {
    private int id;
    private LocalDateTime date;
    private List<Participant> participants;
    private Sport sport;

    public Game(int id, LocalDateTime date, List<Participant> participants, Sport sport) {
        this.id = id;
        this.date = date;
        this.participants = new ArrayList<>(participants);
        this.sport = new Sport(sport);
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<Participant> getPlayers() {
        return new ArrayList<>(participants);
    }

    public Sport getSport() {
        return new Sport(sport);
    }
}

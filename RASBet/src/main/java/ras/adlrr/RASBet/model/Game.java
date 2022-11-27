package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AccessLevel;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id")
    private Set<Participant> participants = null;

    @ManyToOne(optional = false)
    @JoinColumn(updatable = false, nullable = false)
    @JsonIncludeProperties({"id"})
    private Sport sport;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "game", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<GameChoice> gameChoices;

    @Getter(AccessLevel.NONE)
    private String extID;
    private Integer winner_id;
    private String title;
    private LocalDateTime date;
    private int state;

    public static final int OPEN = 1;
    public static final int CLOSED = 2;
    public static final int SUSPENDED = 3;

    public Game(@JsonProperty("id") int id, @JsonProperty("extID") String extID, @JsonProperty("date") LocalDateTime date,
                @JsonProperty("state") int state, @JsonProperty("title") String title, @JsonProperty("sport_id") String sport_id,
                @JsonProperty("participants") Set<Participant> participants){
        this.id = id;
        this.extID = extID;
        this.date = date;
        this.state = state;
        this.title = title;
        this.sport = new Sport();
        sport.setId(sport_id);
        this.participants = participants;
    }

    public Game(int id, String extID, LocalDateTime date,
                int state, String sport_id, String title, Set<Participant> participants,
                List<GameChoice> gameChoices){
        this.id = id;
        this.extID = extID;
        this.date = date;
        this.state = state;
        this.title = title;
        this.sport = new Sport(); sport.setId(sport_id);
        this.participants = participants;
        this.gameChoices = gameChoices;
    }

    public Game(String extID, LocalDateTime date, int state, String title,
                String sport_id, Set<Participant> participants){
        this.extID = extID;
        this.date = date;
        this.state = state;
        this.title = title;
        this.sport = new Sport();
        sport.setId(sport_id);
        this.participants = participants;
    }

    @JsonIgnore
    public String getExtID(){
        return this.extID;
    }

    // ------ Additional Methods ------

    public void addParticipantToGame(Participant p){
        if(participants == null)
            participants = new HashSet<>();
        this.participants.add(p);
    }

    public void decideWinner(){
        int sportType = this.sport.getType();

        int winner = 0;
        switch(sportType){
            case Sport.RACE:
                winner = decideWinnerRace();         break;
            case Sport.WITHOUT_DRAW:
                winner = decideWinnerWithoutDraw();  break;
            case Sport.WITH_DRAW:
                winner = decideWinnerWithDraw();     break;
        }

        this.winner_id = winner;
    }

    private int decideWinnerRace(){
        int winner = 0;
        int scoreWinner = Integer.MAX_VALUE;

        for(Participant p: this.participants){
            if (scoreWinner > p.getScore()){
                winner = p.getId();
                scoreWinner = p.getScore();
            }
        }

        return winner;
    }

    private int decideWinnerWithDraw(){
        int idDraw = 0, idTeamOne = 0, idTeamTwo = 0;
        String teamOne = "", teamTwo = "";
        int scoreT1 = 0, scoreT2 = 0;

        for(Participant p: this.participants){
            if (p.getName().equals("draw"))
                idDraw = p.getId();
            else if (teamOne.equals("")){
                scoreT1 = p.getScore();
                idTeamOne = p.getId();
                teamOne = p.getName();
            }
            else{
                scoreT2 = p.getScore();
                idTeamTwo = p.getId();
                teamTwo = p.getName();
            }
        }

        if (scoreT1 > scoreT2)
            return idTeamOne;
        else if (scoreT2 > scoreT1)
            return idTeamTwo;
        else
            return idDraw;
    }

    private int decideWinnerWithoutDraw(){
        int idTeamOne = 0, idTeamTwo = 0;
        String teamOne = "", teamTwo = "";
        int scoreT1 = 0, scoreT2 = 0;

        for(Participant p: this.participants){
            if (teamOne.equals("")){
                scoreT1 = p.getScore();
                idTeamOne = p.getId();
            }
            else{
                scoreT2 = p.getScore();
                idTeamTwo = p.getId();
            }
        }

        if (scoreT1 > scoreT2)
            return idTeamOne;
        else if (scoreT2 > scoreT2)
            return idTeamTwo;
        else
            return -1;
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", sport=" + sport +
                ", gameChoices=" + gameChoices +
                ", extID='" + extID + '\'' +
                ", winner_id=" + winner_id +
                ", title='" + title + '\'' +
                ", date=" + date +
                ", state=" + state +
                '}';
    }
}
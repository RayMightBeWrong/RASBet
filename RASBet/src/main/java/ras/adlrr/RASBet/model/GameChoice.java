package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "game_choices")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"id", "odd", "game", "participant"})
public class GameChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "game_id", nullable = false, updatable = false)
    @JsonIncludeProperties("id")
    private Game game;

    @ManyToOne(optional = false)
    @JoinColumn(name = "participant_id", nullable = false, updatable = false)
    @JsonIgnoreProperties({"odd"})
    private Participant participant;

    private float odd;

    public GameChoice(@JsonProperty("game_id") int game_id, @JsonProperty("participant_id") int participant_id, @JsonProperty("odd") float odd){
        game = new Game(); game.setId(game_id);
        participant = new Participant(); participant.setId(participant_id);
        this.odd = odd;
    }
}

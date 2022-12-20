package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="game_notifications")
public class GameNotification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gambler_id", updatable = false, nullable = false)
    @JsonIncludeProperties("id")
    private Gambler gambler;

    private String type;
    private String msg;
    private LocalDateTime timestamp;

    public GameNotification(@JsonProperty("gambler_id") int gambler_id, @JsonProperty("type") String type, @JsonProperty("msg") String msg, @JsonProperty("timestamp") LocalDateTime timestamp) {
        gambler = new Gambler();
        gambler.setId(gambler_id);
        this.type = type;
        this.msg = msg;
        this.timestamp = timestamp;
    }
}

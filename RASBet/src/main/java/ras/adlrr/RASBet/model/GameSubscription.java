package ras.adlrr.RASBet.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "game_subscriptions")
public class GameSubscription {

    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class GameSubscriptionID implements Serializable {
        private int gambler_id;
        private int game_id;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            GameSubscriptionID that = (GameSubscriptionID) o;
            return gambler_id == that.gambler_id && game_id == that.game_id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(gambler_id, game_id);
        }
    }

    @EmbeddedId
    private GameSubscriptionID id;

    @MapsId("gambler_id")
    @ManyToOne(optional = false)
    @JoinColumn(name = "gambler_id", updatable = false, nullable = false)
    private Gambler gambler;

    @MapsId("game_id")
    @ManyToOne(optional = false)
    @JoinColumn(name = "game_id", updatable = false, nullable = false)
    private Game game;

    public GameSubscription(int gambler_id, int game_id){
        this.id = new GameSubscriptionID(gambler_id, game_id);
        this.gambler = new Gambler(); this.gambler.setId(gambler_id);
        this.game = new Game(); this.game.setId(game_id);
    }
}

package ras.adlrr.RASBet.model.Promotions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ras.adlrr.RASBet.model.Gambler;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "claimed_promos")
public class ClaimedPromo {
    @Embeddable
    public static class ClaimedPromoID implements Serializable {
        public int promotionId;
        public int gamblerId;
    }

    @EmbeddedId
    private ClaimedPromoID claimedPromoID;

    @ManyToOne(optional = false)
    @JoinColumn(name = "promotion_id", insertable = false, updatable = false, nullable = false)
    @MapsId("promotionId")
    private Promotion promotion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gambler_id", insertable = false, updatable = false, nullable = false)
    @MapsId("gamblerId")
    private Gambler gambler;
}

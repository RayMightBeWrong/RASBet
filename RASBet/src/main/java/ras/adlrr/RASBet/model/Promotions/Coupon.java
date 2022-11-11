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
@Table(name = "coupons")
public class Coupon {
    @Embeddable
    public static class CouponID implements Serializable {
        public int promotionId;
        public int gamblerId;
    }

    @EmbeddedId
    private CouponID couponID;

    @ManyToOne(optional = false)
    @JoinColumn(name = "promotion_id", insertable = false, updatable = false, nullable = false)
    @MapsId("promotionId")
    private Promotion promotion;

    @ManyToOne(optional = false)
    @JoinColumn(name = "gambler_id", insertable = false, updatable = false, nullable = false)
    @MapsId("gamblerId")
    private Gambler gambler;

    private int nr_uses_left; //Number of times left to use the coupon
}

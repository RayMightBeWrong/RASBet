package ras.adlrr.RASBet.model.Promotions.ReferralPromotions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ras.adlrr.RASBet.model.Promotions.Promotion;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "referral_boost_odd_promotions")
public class ReferralBoostOddPromotion extends Promotion {
    private int number_of_referrals_needed;
    private int nr_uses; //nr of uses allowed for the coupon generated
    private float boost_percentage;

    public ReferralBoostOddPromotion(@JsonProperty("title") String title, @JsonProperty("description") String description,
                                     @JsonProperty("beginDate") LocalDateTime beginDate, @JsonProperty("expirationDate") LocalDateTime expirationDate,
                                     @JsonProperty("number_of_referrals_needed") int number_of_referrals_needed, @JsonProperty("nr_uses") int nr_uses,
                                     @JsonProperty("boost_percentage") float boost_percentage) {
        super(title, description, beginDate, expirationDate);
        this.number_of_referrals_needed = number_of_referrals_needed;
        this.nr_uses = nr_uses;
        this.boost_percentage = boost_percentage;
    }
}

package ras.adlrr.RASBet.model.Promotions.ReferralPromotions;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ras.adlrr.RASBet.model.Promotions.interfaces.IBoostOddPromotion;
import ras.adlrr.RASBet.model.Promotions.interfaces.IReferralPromotion;
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
@JsonIgnoreProperties(value = "boostOddPercentage")
public class ReferralBoostOddPromotion extends Promotion implements IBoostOddPromotion, IReferralPromotion {
    private int number_of_referrals_needed;
    private float boost_percentage;

    public ReferralBoostOddPromotion(@JsonProperty("title") String title, @JsonProperty("description") String description,
                                     @JsonProperty("begin_date") LocalDateTime begin_date, @JsonProperty("expiration_date") LocalDateTime expiration_date,
                                     @JsonProperty("nr_uses") int nr_uses, @JsonProperty("coupon") String coupon,
                                     @JsonProperty("number_of_referrals_needed") int number_of_referrals_needed,
                                     @JsonProperty("boost_percentage") float boost_percentage) {
        super(title, description, begin_date, expiration_date, nr_uses, coupon);
        this.number_of_referrals_needed = number_of_referrals_needed;
        this.boost_percentage = boost_percentage;
    }

    @Override
    public float getBoostOddPercentage() {
        return getBoost_percentage();
    }

    @Override
    public void setBoostOddPercentage(float boost_percentage) {
        setBoost_percentage(boost_percentage);
    }
}

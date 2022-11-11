package ras.adlrr.RASBet.model.Promotions.ReferralPromotions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ras.adlrr.RASBet.model.Promotions.Promotion;

import javax.persistence.Entity;
import javax.persistence.Table;

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
}

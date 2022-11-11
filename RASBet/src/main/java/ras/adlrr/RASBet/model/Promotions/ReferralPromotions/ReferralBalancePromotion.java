package ras.adlrr.RASBet.model.Promotions.ReferralPromotions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ras.adlrr.RASBet.model.Coin;
import ras.adlrr.RASBet.model.Promotions.Promotion;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "referral_balance_promotions")
public class ReferralBalancePromotion extends Promotion {
    private int number_of_referrals_needed;
    private float value_to_give;
    @ManyToOne
    @JoinColumn(name = "coin_id", updatable = false, insertable = false)
    private Coin coin;
}

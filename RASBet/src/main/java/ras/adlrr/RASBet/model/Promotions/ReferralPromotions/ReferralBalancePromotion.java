package ras.adlrr.RASBet.model.Promotions.ReferralPromotions;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ras.adlrr.RASBet.model.Coin;
import ras.adlrr.RASBet.model.Promotions.interfaces.IBalancePromotion;
import ras.adlrr.RASBet.model.Promotions.interfaces.IReferralPromotion;
import ras.adlrr.RASBet.model.Promotions.Promotion;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "referral_balance_promotions")
public class ReferralBalancePromotion extends Promotion implements IBalancePromotion, IReferralPromotion {
    private int number_of_referrals_needed;
    private float value_to_give;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "coin_id", updatable = false)
    @JsonIncludeProperties("id")
    private Coin coin;

    public ReferralBalancePromotion(@JsonProperty("title") String title, @JsonProperty("description") String description,
                                    @JsonProperty("begin_date") LocalDateTime beginDate, @JsonProperty("expiration_date") LocalDateTime expirationDate,
                                    @JsonProperty("nr_uses") int nr_uses, @JsonProperty("coupon") String coupon,
                                    @JsonProperty("number_of_referrals_needed") int number_of_referrals_needed,
                                    @JsonProperty("value_to_give") float value_to_give, @JsonProperty("coin_id") String coin_id) {
        super(title, description, beginDate, expirationDate, nr_uses, coupon);
        this.number_of_referrals_needed = number_of_referrals_needed;
        this.value_to_give = value_to_give;
        this.coin = new Coin(); this.coin.setId(coin_id);
    }
}

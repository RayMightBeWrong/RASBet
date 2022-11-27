package ras.adlrr.RASBet.service.interfaces.Promotions;

import java.util.List;

import ras.adlrr.RASBet.model.Promotions.ClaimedPromo;
import ras.adlrr.RASBet.model.Promotions.Promotion;

public interface IClientPromotionService {
    public ClaimedPromo signUpToPromotion(int gambler_id, int promotion_id) throws Exception;

    public List<Promotion> getGamblerListOfClaimedPromotions(int gambler_id);

    public List<Promotion> getGamblerListOfClaimedPromotionsWithUsesLeft(int gambler_id);

    public void claimPromotion(int gambler_id, int promotion_id) throws Exception;

    public void claimPromotionWithCoupon(int gambler_id, String coupon) throws Exception;
}

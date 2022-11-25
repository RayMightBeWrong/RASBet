package ras.adlrr.RASBet.model.Promotions;

public interface IBoostOddPromotion extends IPromotion {
    float getBoostOddPercentage();
    void setBoostOddPercentage(float boost_percentage);
}

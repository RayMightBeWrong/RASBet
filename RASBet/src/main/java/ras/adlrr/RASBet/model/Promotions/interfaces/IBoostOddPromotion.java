package ras.adlrr.RASBet.model.Promotions.interfaces;

public interface IBoostOddPromotion extends IPromotion {
    float getBoostOddPercentage();
    void setBoostOddPercentage(float boost_percentage);
}

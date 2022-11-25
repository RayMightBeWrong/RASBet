package ras.adlrr.RASBet.model.Promotions;

import ras.adlrr.RASBet.model.Coin;

public interface IBalancePromotion extends IPromotion{
    float getValue_to_give();
    Coin getCoin();
    void setValue_to_give(float value);
    void setCoin(Coin coin);
}

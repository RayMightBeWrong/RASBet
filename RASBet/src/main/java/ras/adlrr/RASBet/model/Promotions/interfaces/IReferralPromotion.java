package ras.adlrr.RASBet.model.Promotions.interfaces;

public interface IReferralPromotion extends IPromotion{
    int getNumber_of_referrals_needed();
    void setNumber_of_referrals_needed(int nr_referrals);
}

package ras.adlrr.RASBet.service.interfaces;

import ras.adlrr.RASBet.model.Gambler;

public interface IUserReferralService {
    Gambler createGambler(Integer referral, Gambler gambler) throws Exception;
}

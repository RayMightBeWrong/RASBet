package ras.adlrr.RASBet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.model.Gambler;
import ras.adlrr.RASBet.service.interfaces.IGamblerService;
import ras.adlrr.RASBet.service.interfaces.IUserReferralService;
import ras.adlrr.RASBet.service.interfaces.Promotions.IReferralService;

import javax.transaction.Transactional;

@RequiredArgsConstructor
@Service
public class UserReferralService implements IUserReferralService {

    private final IGamblerService gamblerService;
    private final IReferralService referralService;

    @Transactional(rollbackOn = {Exception.class, DataAccessException.class}, value = Transactional.TxType.REQUIRES_NEW)
    @Override
    public Gambler createGambler(Integer referral, Gambler gambler) throws Exception {
        gambler = gamblerService.addGambler(gambler);
        if(referral != null)
            referralService.addReferral(referral, gambler.getId());
        return gambler;
    }
}

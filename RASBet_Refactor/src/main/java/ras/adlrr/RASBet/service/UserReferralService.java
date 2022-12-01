package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.model.Gambler;
import ras.adlrr.RASBet.service.interfaces.users.IGamblerService;
import ras.adlrr.RASBet.service.interfaces.IUserReferralService;
import ras.adlrr.RASBet.service.interfaces.promotions.IReferralService;

import javax.transaction.Transactional;

@Service("userReferralService")
public class UserReferralService implements IUserReferralService {
    private final IGamblerService gamblerService;
    private final IReferralService referralService;

    public UserReferralService(@Qualifier("userFacade") IGamblerService gamblerService,
                               @Qualifier("promotionsFacade") IReferralService referralService) {
        this.gamblerService = gamblerService;
        this.referralService = referralService;
    }

    @Transactional(rollbackOn = {Exception.class, DataAccessException.class}, value = Transactional.TxType.REQUIRES_NEW)
    @Override
    public Gambler createGambler(Integer referral, Gambler gambler) throws Exception {
        gambler = gamblerService.addGambler(gambler);
        if(referral != null)
            referralService.addReferral(referral, gambler.getId());
        return gambler;
    }
}

package ras.adlrr.RASBet.service;

import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.ReferralRepository;
import ras.adlrr.RASBet.model.Promotions.Referral;

import java.time.LocalDateTime;

@Service
public class ReferralService {

    private final ReferralRepository referralRepository;

    public ReferralService(ReferralRepository referralRepository) {
        this.referralRepository = referralRepository;
    }

    public void addReferral(int referredId, int referrerId) throws Exception{
        Referral referral = new Referral(referredId, referrerId, LocalDateTime.now());
        if(referralRepository.existsById(new Referral.ReferralID(referredId, referrerId)))
            throw new Exception("Referral already exists!");
        referralRepository.save(referral);
    }
}

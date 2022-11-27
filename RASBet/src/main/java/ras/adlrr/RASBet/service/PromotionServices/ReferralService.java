package ras.adlrr.RASBet.service.PromotionServices;

import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.ReferralRepository;
import ras.adlrr.RASBet.model.Promotions.Referral;
import ras.adlrr.RASBet.service.interfaces.Promotions.IReferralService;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class ReferralService implements IReferralService{

    private final ReferralRepository referralRepository;

    public ReferralService(ReferralRepository referralRepository) {
        this.referralRepository = referralRepository;
    }

    /**
     * Add referral
     * @param referredId Identification of the gambler that "referred" the application to the new gambler ("referrer")
     * @param referrerId Identification of the new gambler
     * @throws Exception If the referrer is the same as the referred. If the referrer already referred someone. If the referral already exists.
     */
    public void addReferral(int referredId, int referrerId) throws Exception{
        Referral referral = new Referral(referredId, referrerId, LocalDateTime.now(ZoneId.of("UTC+00:00")));
        if(referredId == referrerId)
            throw new Exception("A user cannot refer himself.");
        if(referralRepository.countTimesAsReferrer(referrerId) != 0)
            throw new Exception("A user cannot refer more than 1 person.");
        if(referralRepository.existsById(new Referral.ReferralID(referredId, referrerId)))
            throw new Exception("Referral already exists!");
        referralRepository.save(referral);
    }

    /**
     * @param gambler_id Identification of the gambler (referred)
     * @return the number of times a gambler was referred by other gamblers.
     */
    public int getGamblerNrOfReferrals(int gambler_id){
        return referralRepository.countReferredTimesById(gambler_id);
    }

    /**
     * @param gambler_id Identification of the gambler (referred)
     * @return the number of times a gambler was referred by other gamblers between dates.
     */
    public int getGamblerNrOfReferralsBetweenDates(int gambler_id, LocalDateTime initial_date, LocalDateTime end_date){
        return referralRepository.countReferredTimesBetweenDatesById(gambler_id, initial_date, end_date);
    }
}

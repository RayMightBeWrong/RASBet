package ras.adlrr.RASBet.service.PromotionServices;

import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.ClaimedPromoRepository;
import ras.adlrr.RASBet.model.Promotions.BoostOddPromotion;
import ras.adlrr.RASBet.model.Promotions.ClaimedPromo;
import ras.adlrr.RASBet.model.Promotions.Promotion;
import ras.adlrr.RASBet.model.Promotions.ReferralPromotions.ReferralBalancePromotion;
import ras.adlrr.RASBet.model.Promotions.ReferralPromotions.ReferralBoostOddPromotion;
import ras.adlrr.RASBet.service.UserService;
import ras.adlrr.RASBet.service.WalletService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ClientPromotionService {
    private final UserService userService;
    private final WalletService walletService;
    private final PromotionService promotionService;
    private final ReferralService referralService;
    private final ClaimedPromoRepository claimedPromoRepository;

    public ClientPromotionService(UserService userService, WalletService walletService, PromotionService promotionService, ReferralService referralService, ClaimedPromoRepository claimedPromoRepository) {
        this.userService = userService;
        this.walletService = walletService;
        this.promotionService = promotionService;
        this.referralService = referralService;
        this.claimedPromoRepository = claimedPromoRepository;
    }

    /**
     *
     * @param gambler_id
     * @param promotion_id
     * @return
     * @throws Exception
     */
    public ClaimedPromo signUpToPromotion(int gambler_id, int promotion_id) throws Exception {
        Promotion promotion = promotionService.getPromotionById(promotion_id);
        if(promotion == null)
            throw new Exception("Promotion with id " + promotion_id + " does not exist!");

        String error;
        if((error = checkSignUpToConditions(gambler_id, promotion)) != null)
            throw new Exception(error);

        return claimedPromoRepository.save(new ClaimedPromo(promotion_id, gambler_id, promotion.getNr_uses()));
    }

    public List<Promotion> getGamblerListOfClaimedPromotions(int gambler_id){
        List<Promotion> ret = new ArrayList<>();
        List<Integer> claimedPromos = claimedPromoRepository.getAllClaimedPromotionsIdsByGamblerId(gambler_id);
        claimedPromos.forEach( (Integer cp) -> {
            Promotion p;
            if((p = promotionService.getPromotionById(cp)) != null)
                ret.add(p);
        });
        return ret;
    }

    public List<Promotion> getGamblerListOfClaimedPromotionsWithUsesLeft(int gambler_id){
        List<Promotion> ret = new ArrayList<>();
        List<Integer> claimedPromos = claimedPromoRepository.getAllClaimedPromotionsIdsWithUsesLeftByGamblerId(gambler_id);
        claimedPromos.forEach( (Integer cp) -> {
            Promotion p;
            if((p = promotionService.getPromotionById(cp)) != null)
                ret.add(p);
        });
        return ret;
    }


    public void claimPromotion(int gambler_id, int promotion_id) throws Exception {
        ClaimedPromo claimedPromo = claimedPromoRepository.findById(new ClaimedPromo.ClaimedPromoID(gambler_id, promotion_id))
                                                          .orElse(null);
        if(claimedPromo == null)
            claimedPromo = signUpToPromotion(gambler_id, promotion_id);

        int nr_uses_left = claimedPromo.getNr_uses_left();
        if(nr_uses_left <= 0)
            throw new Exception("Promotion already claimed.");

        claimedPromo.setNr_uses_left(nr_uses_left - 1);
        claimedPromoRepository.save(claimedPromo);
    }

    public void claimPromotionWithCoupon(int gambler_id, String coupon) throws Exception {
        Promotion promotion = promotionService.getPromotionByCoupon(coupon);
        if(promotion == null)
            throw new Exception("The given coupon is not associated to a promotion.");
        claimPromotion(gambler_id, promotion.getId());
    }

    // ************ Auxiliary Functions ************ //

    /**
     *
     * @param gambler_id
     * @param promotion
     * @return
     */
    private String checkSignUpToConditions(int gambler_id, Promotion promotion) {
        if(!userService.gamblerExistsById(gambler_id))
            return "Gambler with id " + gambler_id + "does not exist!";

        //Check if the gambler already claimed the promotion
        ClaimedPromo claimedPromo = claimedPromoRepository.findById(new ClaimedPromo.ClaimedPromoID(gambler_id, promotion.getId()))
                                                          .orElse(null);
        if(claimedPromo != null)
            return "Gambler already applied to the promotion.";

        String error = null;

        if (promotion instanceof ReferralBalancePromotion)
            error = checkReferralBalancePromoSignUpToConditions(gambler_id, (ReferralBalancePromotion) promotion);
        else if (promotion instanceof ReferralBoostOddPromotion)
            error = checkReferralBoostOddPromoSignUpToConditions(gambler_id, (ReferralBoostOddPromotion) promotion);
        //else if (promotion instanceof BoostOddPromotion) // Does not have any additional conditions that need to be verified
        //    error = checkBoostOddPromoSignUpToConditions();

        return error;
    }

    private String checkReferralPromoSignUpToConditions(int gambler_id, LocalDateTime begin_date, int minimum_of_referrals) {
        if(referralService.getGamblerNrOfReferralsBetweenDates(gambler_id, begin_date, LocalDateTime.now()) >= minimum_of_referrals)
            return null;
        return "Gambler has not been referred enough times since the start of the promotion.";
    }

    private String checkReferralBoostOddPromoSignUpToConditions(int gambler_id, ReferralBoostOddPromotion promotion) {
        return checkReferralPromoSignUpToConditions(gambler_id, promotion.getBegin_date(), promotion.getNumber_of_referrals_needed());
    }

    private String checkReferralBalancePromoSignUpToConditions(int gambler_id, ReferralBalancePromotion promotion) {
        return checkReferralPromoSignUpToConditions(gambler_id, promotion.getBegin_date(), promotion.getNumber_of_referrals_needed());
    }


}

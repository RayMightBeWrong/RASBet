package ras.adlrr.RASBet.service;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.PromotionRepository;
import ras.adlrr.RASBet.model.Promotions.BoostOddPromotion;
import ras.adlrr.RASBet.model.Promotions.Promotion;
import ras.adlrr.RASBet.model.Promotions.ReferralPromotions.ReferralBalancePromotion;
import ras.adlrr.RASBet.model.Promotions.ReferralPromotions.ReferralBoostOddPromotion;

import java.time.LocalDateTime;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;

    public PromotionService(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    //public BoostOddPromotion createBoostOddPromotion(BoostOddPromotion boostOddPromotion){
//
    //}
//

    /**
     * Persists a promotion in the repository.
     * @param promotion Promotion to be persisted
     * @return promotion updated by the database
     * @throws Exception If there is any attribute that does not meet the requirements.
     */
    public Promotion createPromotion(Promotion promotion) throws Exception {
        if (promotion == null)
            throw new Exception("Promotion is null!");
        promotion.setId(0); //Avoids the update of an existing promotion

        String error = checkPromotionBasicAttributes(promotion);
        if (error != null)
            throw new Exception(error);

        error = checkSpecificAttributes(promotion);
        if (error != null)
            throw new Exception(error);

        try {
            return promotionRepository.save(promotion);
        } catch (DataAccessException dae){
            throw new Exception("Not a valid promotion!");
        }
    }


    /**
     * @param promotion Promotion, which specific attributes need to be verified.
     * @return null if all the specific attributes are valid. Otherwise, returns a string with the error.
     */
    private String checkSpecificAttributes(Promotion promotion) {
        if(promotion instanceof BoostOddPromotion)
            return checkBoostOddPromotionSpecificAttributes((BoostOddPromotion) promotion);
        else if (promotion instanceof ReferralBoostOddPromotion)
            return checkReferralBoostOddPromotionSpecificAttributes((ReferralBoostOddPromotion) promotion);
        else if (promotion instanceof ReferralBalancePromotion)
            return checkReferralBalancePromotionSpecificAttributes((ReferralBalancePromotion) promotion);
        else
            return "Not a valid promotion!";
    }

    private String checkReferralBalancePromotionSpecificAttributes(ReferralBalancePromotion referralBalancePromotion) {
        return null; //TODO
    }

    private String checkReferralBoostOddPromotionSpecificAttributes(ReferralBoostOddPromotion referralBoostOddPromotion) {
        return null; //TODO
    }

    private String checkBoostOddPromotionSpecificAttributes(BoostOddPromotion boostOddPromotion) {
        return null; //TODO
    }

    /**
     * @param promotion Promotion, which basic attributes need to be verified.
     * @return null if all the basic attributes are valid. Otherwise, returns a string with the error.
     */
    private String checkPromotionBasicAttributes(Promotion promotion){
        if(promotion.getTitle() == null)
           return "Title cannot be null.";
        if(promotion.getDescription() == null)
           return "Description cannot be null.";
        if(promotion.getBeginDate() == null)
           return "Begin date cannot be null.";
        if(promotion.getExpirationDate() != null && promotion.getBeginDate().isAfter(promotion.getExpirationDate()))
           return "Begin date must be before the expiration date.";
        if(promotion.getNr_uses() <= 0)
           return "The number of times a promotion can be redeemed must be a positive.";
        if(promotionRepository.existsByCoupon(promotion.getCoupon()))
           return "Coupon already in use!";
        return null;
    }
}

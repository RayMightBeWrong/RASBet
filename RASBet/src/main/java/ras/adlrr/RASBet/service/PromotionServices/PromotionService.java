package ras.adlrr.RASBet.service.PromotionServices;

import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.ClaimedPromoRepository;
import ras.adlrr.RASBet.dao.PromotionRepository;
import ras.adlrr.RASBet.model.Promotions.BoostOddPromotion;
import ras.adlrr.RASBet.model.Promotions.Promotion;
import ras.adlrr.RASBet.model.Promotions.ReferralPromotions.ReferralBalancePromotion;
import ras.adlrr.RASBet.model.Promotions.ReferralPromotions.ReferralBoostOddPromotion;
import ras.adlrr.RASBet.service.interfaces.ICoinService;
import ras.adlrr.RASBet.service.interfaces.Promotions.IPromotionService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromotionService implements IPromotionService{
    private final PromotionRepository promotionRepository;
    private final ClaimedPromoRepository claimedPromoRepository;
    private final ICoinService coinService;

    public PromotionService(PromotionRepository promotionRepository, ICoinService coinService, ClaimedPromoRepository claimedPromoRepository) {
        this.promotionRepository = promotionRepository;
        this.coinService = coinService;
        this.claimedPromoRepository = claimedPromoRepository;
    }

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
     * Remove promotion by id
     * @param promotion_id Identification of the promotion
     */
    @Transactional
    public void removePromotion(int promotion_id) throws Exception{
        if(!promotionRepository.existsById(promotion_id))
            throw new Exception("Promotion does not exist!");
        claimedPromoRepository.deleteAllByPromotionId(promotion_id);
        promotionRepository.deleteById(promotion_id);
    }

    /**
     * @param promotion_id Identification of the promotion
     * @return promotion which identification matches the promotion_id given
     */
    public Promotion getPromotionById(int promotion_id){
        return promotionRepository.findById(promotion_id).orElse(null);
    }

    /**
     * @param promotion_id Identification of the promotion
     * @return true if a promotion with the given identification exists.
     */
    public boolean existsPromotionById(int promotion_id){
        return promotionRepository.existsById(promotion_id);
    }

    /**
     * @param coupon Coupon that a promotion should have associated
     * @return promotion that has given coupon associated.
     */
    public Promotion getPromotionByCoupon(String coupon){
        return promotionRepository.findByCoupon(coupon);
    }

    /**
     * @param coupon Coupon that a promotion should have associated
     * @return true if a promotion with the given coupon exists.
     */
    public boolean existsPromotionByCoupon(String coupon){
        return promotionRepository.existsByCoupon(coupon);
    }

    /**
     * @return List of all the promotions present in the repository.
     */
    public List<Promotion> getAllPromotions(){
        return promotionRepository.findAll();
    }

    /**
     * @param whichDate Which date should be used to order the promotions. "begin_date" or "expiration_date".
     * @param direction Direction of the order, ASC or DESC.
     * @return all the promotions ordered by the start date or expiration date. A null value is returned if the argument "whichDate" is invalid.
     */
    public List<Promotion> getAllPromotionsOrderedByDate(String whichDate, Sort.Direction direction) throws Exception {
        if(!whichDate.equals("beginDate") && !whichDate.equals("expirationDate"))
            throw new Exception("Parameter \"which_date\" can either be \"beginDate\" or \"expirationDate\"");
        return promotionRepository.findAll(Sort.by(direction, whichDate));
    }

    /**
     * @param which_date Can either be "begin_date" or "expiration_date". If another value is given, "begin_date" is assumed. Value used to choose the date that should be used to search.
     * @param startDate Date that defines the start of the interval (inclusive)
     * @param endDate Date that defines the end of the interval (inclusive)
     * @param direction Direction of the order, ASC or DESC.
     * @return list of ordered promotions which begin date (or expiration date, depending on the value of the parameter "which_date") is contained between dates.
     */
    public List<Promotion> getPromotionsBetweenDatesOrderedByDate(String which_date, LocalDateTime startDate, LocalDateTime endDate, Sort.Direction direction) throws Exception {
        if(!which_date.equals("begin_date") && !which_date.equals("expiration_date"))
            throw new Exception("Parameter \"which_date\" can either be \"begin_date\" or \"expiration_date\"");
        return promotionRepository.getPromotionsBetweenDatesOrderedByDate(which_date, startDate, endDate, Sort.by(direction, which_date));
    }


    // ---------- Auxiliary Functions ---------- //

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

    /**
     * @param referralBalancePromotion Promotion, which specific attributes need to be verified.
     * @return null if all the specific attributes are valid. Otherwise, returns a string with the error.
     */
    private String checkReferralBalancePromotionSpecificAttributes(ReferralBalancePromotion referralBalancePromotion) {
        if(referralBalancePromotion.getNumber_of_referrals_needed() <= 0)
            return "Number of referrals needed to claim the promotion should be positive.";
        if(referralBalancePromotion.getValue_to_give() <= 0)
            return "Value should be positive.";
        if(referralBalancePromotion.getCoin() == null || !coinService.coinExistsById(referralBalancePromotion.getCoin().getId()))
            return "A valid coin is needed.";
        return null;
    }

    /**
     * @param referralBoostOddPromotion Promotion, which specific attributes need to be verified.
     * @return null if all the specific attributes are valid. Otherwise, returns a string with the error.
     */
    private String checkReferralBoostOddPromotionSpecificAttributes(ReferralBoostOddPromotion referralBoostOddPromotion) {
        if(referralBoostOddPromotion.getNumber_of_referrals_needed() <= 0)
            return "Number of referrals needed to claim the promotion should be positive.";
        if(referralBoostOddPromotion.getBoost_percentage() <= 0 || referralBoostOddPromotion.getBoost_percentage() > 100)
            return "The boost percentage should be a value between 0 (exclusive) and 100 (inclusive).";
        return null;
    }

    /**
     * @param boostOddPromotion Promotion, which specific attributes need to be verified.
     * @return null if all the specific attributes are valid. Otherwise, returns a string with the error.
     */
    private String checkBoostOddPromotionSpecificAttributes(BoostOddPromotion boostOddPromotion) {
        if(boostOddPromotion.getBoost_percentage() <= 0 || boostOddPromotion.getBoost_percentage() > 100)
            return "The boost percentage should be a value between 0 (exclusive) and 100 (inclusive).";
        return null;
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
        if(promotion.getCoupon() == null)
            return "Coupon cannot be null!";
        if(promotionRepository.existsByCoupon(promotion.getCoupon()))
           return "Coupon already in use!";
        return null;
    }
}

package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.model.Promotions.ClaimedPromo;
import ras.adlrr.RASBet.model.Promotions.Promotion;
import ras.adlrr.RASBet.service.interfaces.users.IGamblerService;
import ras.adlrr.RASBet.service.interfaces.promotions.IClientPromotionService;
import ras.adlrr.RASBet.service.interfaces.promotions.IPromotionService;
import ras.adlrr.RASBet.service.interfaces.promotions.IReferralService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service("promotionsFacade")
public class PromotionsFacade implements IPromotionService, IClientPromotionService, IReferralService {
    private final IPromotionService promotionService;
    private final IClientPromotionService clientPromotionService;
    private final IReferralService referralService;
    private final IGamblerService gamblerService;

    @Autowired
    public PromotionsFacade(@Qualifier("promotionService") IPromotionService promotionService,
                            @Qualifier("clientPromotionService") IClientPromotionService clientPromotionService,
                            @Qualifier("referralService") IReferralService referralService,
                            @Qualifier("userFacade") IGamblerService gamblerService) {
        this.promotionService = promotionService;
        this.clientPromotionService = clientPromotionService;
        this.referralService = referralService;
        this.gamblerService = gamblerService;
    }

    /* ******* Promotion Methods ******* */

    /**
     * Persists a promotion in the repository.
     * @param promotion Promotion to be persisted
     * @return promotion updated by the database
     * @throws Exception If there is any attribute that does not meet the requirements.
     */
    public Promotion createPromotion(Promotion promotion) throws Exception {
        return promotionService.createPromotion(promotion);
    }

    /**
     * Remove promotion by id
     * @param promotion_id Identification of the promotion
     */
    @Transactional
    public void removePromotion(int promotion_id) throws Exception{
        promotionService.removePromotion(promotion_id);
    }

    /**
     * @param promotion_id Identification of the promotion
     * @return promotion which identification matches the promotion_id given
     */
    public Promotion getPromotionById(int promotion_id){
        return promotionService.getPromotionById(promotion_id);
    }

    /**
     * @param promotion_id Identification of the promotion
     * @return true if a promotion with the given identification exists.
     */
    public boolean existsPromotionById(int promotion_id){
        return promotionService.existsPromotionById(promotion_id);
    }

    /**
     * @param coupon Coupon that a promotion should have associated
     * @return promotion that has given coupon associated.
     */
    public Promotion getPromotionByCoupon(String coupon){
        return promotionService.getPromotionByCoupon(coupon);
    }

    /**
     * @param coupon Coupon that a promotion should have associated
     * @return true if a promotion with the given coupon exists.
     */
    public boolean existsPromotionByCoupon(String coupon){
        return promotionService.existsPromotionByCoupon(coupon);
    }

    /**
     * @return List of all the promotions present in the repository.
     */
    public List<Promotion> getAllPromotions(){
        return promotionService.getAllPromotions();
    }

    /**
     * @param whichDate Which date should be used to order the promotions. "begin_date" or "expiration_date".
     * @param direction Direction of the order, ASC or DESC.
     * @return all the promotions ordered by the start date or expiration date. A null value is returned if the argument "whichDate" is invalid.
     */
    public List<Promotion> getAllPromotionsOrderedByDate(String whichDate, Sort.Direction direction) throws Exception {
        return promotionService.getAllPromotionsOrderedByDate(whichDate, direction);
    }

    /**
     * @param which_date Can either be "begin_date" or "expiration_date". If another value is given, "begin_date" is assumed. Value used to choose the date that should be used to search.
     * @param startDate Date that defines the start of the interval (inclusive)
     * @param endDate Date that defines the end of the interval (inclusive)
     * @param direction Direction of the order, ASC or DESC.
     * @return list of ordered promotions which begin date (or expiration date, depending on the value of the parameter "which_date") is contained between dates.
     */
    public List<Promotion> getPromotionsBetweenDatesOrderedByDate(String which_date, LocalDateTime startDate, LocalDateTime endDate, Sort.Direction direction) throws Exception {
        return promotionService.getPromotionsBetweenDatesOrderedByDate(which_date, startDate, endDate, direction);
    }


    /* ******* Client Promotion Methods ******* */

    /**
     *
     * @param gambler_id
     * @param promotion_id
     * @return
     * @throws Exception
     */
    public ClaimedPromo signUpToPromotion(int gambler_id, int promotion_id) throws Exception {
        if(!gamblerService.gamblerExistsById(gambler_id))
            throw new Exception("Gambler with id " + gambler_id + "does not exist!");
        return clientPromotionService.signUpToPromotion(gambler_id, promotion_id);
    }

    public List<Promotion> getGamblerListOfClaimedPromotions(int gambler_id){
        return clientPromotionService.getGamblerListOfClaimedPromotions(gambler_id);
    }

    public List<Promotion> getGamblerListOfClaimedPromotionsWithUsesLeft(int gambler_id){
        return clientPromotionService.getGamblerListOfClaimedPromotionsWithUsesLeft(gambler_id);
    }


    public void claimPromotion(int gambler_id, int promotion_id) throws Exception {
        if(!gamblerService.gamblerExistsById(gambler_id))
            throw new Exception("Gambler with id " + gambler_id + "does not exist!");
        clientPromotionService.claimPromotion(gambler_id,promotion_id);
    }

    public void claimPromotionWithCoupon(int gambler_id, String coupon) throws Exception {
        if(!gamblerService.gamblerExistsById(gambler_id))
            throw new Exception("Gambler with id " + gambler_id + "does not exist!");
        clientPromotionService.claimPromotionWithCoupon(gambler_id, coupon);
    }

    /* ******* Referral Methods ******* */

    /**
     * Create gambler with referral
     * @param referredId Identification of the gambler that "referred" the application to the new gambler ("referrer")
     * @param referrerId Identification of the new gambler
     * @throws Exception If the referrer is the same as the referred. If the referrer already referred someone. If the referral already exists.
     */
    public void addReferral(int referredId, int referrerId) throws Exception{
        if(!gamblerService.gamblerExistsById(referredId))
            throw new Exception("Gambler with id " + referredId + "does not exist!");
        if(!gamblerService.gamblerExistsById(referrerId))
            throw new Exception("Gambler with id " + referrerId + "does not exist!");
        referralService.addReferral(referredId, referrerId);
    }

    /**
     * @param gambler_id Identification of the gambler (referred)
     * @return the number of times a gambler was referred by other gamblers.
     */
    public int getGamblerNrOfReferrals(int gambler_id){
        return referralService.getGamblerNrOfReferrals(gambler_id);
    }

    /**
     * @param gambler_id Identification of the gambler (referred)
     * @return the number of times a gambler was referred by other gamblers between dates.
     */
    public int getGamblerNrOfReferralsBetweenDates(int gambler_id, LocalDateTime initial_date, LocalDateTime end_date){
        return referralService.getGamblerNrOfReferralsBetweenDates(gambler_id, initial_date, end_date);
    }
}

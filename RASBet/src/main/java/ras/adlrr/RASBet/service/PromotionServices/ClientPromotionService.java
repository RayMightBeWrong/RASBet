package ras.adlrr.RASBet.service.PromotionServices;

import ras.adlrr.RASBet.dao.ClaimedPromoRepository;
import ras.adlrr.RASBet.model.Promotions.ClaimedPromo;
import ras.adlrr.RASBet.model.Promotions.Promotion;
import ras.adlrr.RASBet.service.UserService;
import ras.adlrr.RASBet.service.WalletService;

import java.util.List;

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
    public boolean applyToPromotion(int gambler_id, int promotion_id) throws Exception {
        if(!userService.gamblerExistsById(gambler_id))
            throw new Exception("Gambler with id " + gambler_id + "does not exist!");

        Promotion promotion = promotionService.getPromotionById(promotion_id);
        if(promotion == null)
            throw new Exception("Promotion with id " + promotion_id + " does not exist!");

        if(!checkClaimConditions(gambler_id, promotion))
            return false;

        claimedPromoRepository.save(new ClaimedPromo(promotion_id, gambler_id, promotion.getNr_uses()));
        return true;
    }

    public List<Promotion> getListOfClaimedPromotions(){
        return null; //TODO
    }

    public List<Promotion> getListOfClaimedPromotionsWithUsesLeft(){
        return null; //TODO
    }

    public boolean claimPromotion(int promotion_id){
        return false; //TODO
    }

    public boolean claimPromotionWithCoupon(String coupon){
        return false; //TODO
    }

    // ************ Auxiliary Functions ************ //

    /**
     *
     * @param gambler_id
     * @param promotion
     * @return
     */
    public boolean checkClaimConditions(int gambler_id, Promotion promotion){
        return false; //TODO
    }
}

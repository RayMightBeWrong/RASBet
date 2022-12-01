package ras.adlrr.RASBet.service.interfaces.promotions;

import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.List;

import ras.adlrr.RASBet.model.Promotions.Promotion;

public interface IPromotionService {
    public Promotion createPromotion(Promotion promotion) throws Exception;

    public void removePromotion(int promotion_id) throws Exception;

    public Promotion getPromotionById(int promotion_id);

    public boolean existsPromotionById(int promotion_id);

    public Promotion getPromotionByCoupon(String coupon);

    public boolean existsPromotionByCoupon(String coupon);

    public List<Promotion> getAllPromotions();

    public List<Promotion> getAllPromotionsOrderedByDate(String whichDate, Sort.Direction direction) throws Exception;

    public List<Promotion> getPromotionsBetweenDatesOrderedByDate(String which_date, LocalDateTime startDate, LocalDateTime endDate, Sort.Direction direction) throws Exception;

    
}

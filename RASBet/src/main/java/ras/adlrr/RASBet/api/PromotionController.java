package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ras.adlrr.RASBet.dao.CouponRepository;
import ras.adlrr.RASBet.dao.PromotionRepository;
import ras.adlrr.RASBet.model.Promotions.BoostOddPromotion;
import ras.adlrr.RASBet.model.Promotions.Coupon;
import ras.adlrr.RASBet.model.Promotions.Promotion;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    private final PromotionRepository promotionRepository;
    private final CouponRepository couponRepository;

    @Autowired
    public PromotionController(PromotionRepository promotionRepository, CouponRepository couponRepository) {
        this.promotionRepository = promotionRepository;
        this.couponRepository = couponRepository;
    }

    @PostMapping
    public void testToInsertSubclass(){
        Promotion boostOdd = new BoostOddPromotion(1,20);
        promotionRepository.save(boostOdd);
    }

    @PostMapping(path = "/coupon")
    public void testToInsertCoupon(){
        Coupon coupon = new Coupon(1, 1, 1);
        couponRepository.save(coupon);
    }

    @GetMapping(path = "/coupon")
    public Coupon testToGetCoupon(){
        Coupon coupon = couponRepository.findById(new Coupon.CouponID(1,1)).orElse(null);
        return coupon;
    }
}

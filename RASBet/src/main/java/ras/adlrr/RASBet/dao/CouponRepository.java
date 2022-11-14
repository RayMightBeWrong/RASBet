package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ras.adlrr.RASBet.model.Promotions.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Coupon.CouponID> {
}

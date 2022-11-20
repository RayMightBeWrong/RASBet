package ras.adlrr.RASBet.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Promotions.Promotion;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion,Integer> {
    Promotion findByCoupon(String coupon);
    boolean existsByCoupon(String coupon);

    /**
     * @param which_date Can either be "begin_date" or "expiration_date". If another value is given, "begin_date" is assumed. Value used to choose the date that should be used to search.
     * @param startDate Date that defines the start of the interval (inclusive)
     * @param endDate Date that defines the end of the interval (inclusive)
     * @param sort Specifies how to order the results
     * @return list of promotions which begin date (or expiration date, depending on the value of the parameter "which_date") is contained between dates.
     */
    @Query("SELECT p FROM Promotion p WHERE CASE WHEN (:which_date = 'expiration_date') THEN p.expiration_date ELSE p.begin_date END BETWEEN :startDate AND :endDate")
    List<Promotion> getPromotionsBetweenDatesOrderedByDate(@Param("which_date") String which_date,
                                                               @Param("startDate") LocalDateTime startDate,
                                                               @Param("endDate") LocalDateTime endDate,
                                                               Sort sort);
}

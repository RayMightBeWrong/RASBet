package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Promotions.Referral;

import java.time.LocalDateTime;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Referral.ReferralID> {

    @Query(value = "SELECT COUNT(ref.referrer_id) FROM referrals AS ref WHERE ref.referred_id = :user_id", nativeQuery = true)
    int countReferredTimesById(@Param("user_id") int user_id);

    @Query(value = "SELECT COUNT(ref.referrer_id) FROM referrals AS ref " +
                   "WHERE ref.referred_id = :user_id AND ref.registration_date " +
                   "BETWEEN :initial_date AND :end_date",
           nativeQuery = true)
    int countReferredTimesBetweenDatesById(@Param("user_id") int user_id,
                                           @Param("initial_date") LocalDateTime initial_date,
                                           @Param("end_date") LocalDateTime end_date);


    @Query(value = "SELECT COUNT(ref.referrer_id) FROM referrals ref WHERE ref.referrer_id = :referrer_id", nativeQuery = true)
    int countTimesAsReferrer(@Param("referrer_id") int referrer_id);
}

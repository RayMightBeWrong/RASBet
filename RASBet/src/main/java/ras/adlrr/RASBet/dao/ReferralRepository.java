package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Promotions.Referral;

@Repository
public interface ReferralRepository extends JpaRepository<Referral, Referral.ReferralID> {
}

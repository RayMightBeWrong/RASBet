package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ras.adlrr.RASBet.model.Promotions.ClaimedPromo;

public interface ClaimedPromoRepository extends JpaRepository<ClaimedPromo, ClaimedPromo.ClaimedPromoID> {
}

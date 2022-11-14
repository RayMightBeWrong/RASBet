package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Promotions.Promotion;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion,Integer> {
}

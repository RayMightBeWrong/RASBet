package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Expert;

@Repository
public interface ExpertRepository extends JpaRepository<Expert,Integer> {
}

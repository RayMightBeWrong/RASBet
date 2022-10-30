package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Gambler;

@Repository
public interface GamblerRepository extends JpaRepository<Gambler,Integer> {
}

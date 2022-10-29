package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ras.adlrr.RASBet.model.Gambler;

public interface GamblerRepository extends JpaRepository<Gambler,Integer> {
}

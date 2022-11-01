package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Expert;
import ras.adlrr.RASBet.model.Gambler;

import java.util.Optional;

@Repository
public interface ExpertRepository extends JpaRepository<Expert,Integer> {
    Optional<Expert> findByEmail(String email);
    boolean existsByEmail(String email);
}

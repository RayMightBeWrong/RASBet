package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Gambler;

import java.util.Optional;

@Repository
public interface GamblerRepository extends JpaRepository<Gambler,Integer> {
    Optional<Gambler> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query(value = "SELECT g.email FROM gambler g WHERE g.id = :gambler_id", nativeQuery = true)
    Optional<String> getGamblerEmail(@Param("gambler_id") int id);
}

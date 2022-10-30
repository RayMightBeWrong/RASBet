package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Gambler;

import java.util.List;

@Repository
public interface GamblerRepository extends JpaRepository<Gambler,Integer> {
    @Query("SELECT r.id FROM Gambler r where r.email = :email")
    List<Integer> findIdByEmail(@Param("email") String email);
}

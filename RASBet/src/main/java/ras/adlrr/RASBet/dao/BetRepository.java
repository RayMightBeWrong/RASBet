package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Bet;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet, Integer> {
    @Query("SELECT b FROM Bet b WHERE b.transaction.gambler.id = :gambler_id")
    List<Bet> findAllByGamblerId(@Param("gambler_id") int gambler_id);
}

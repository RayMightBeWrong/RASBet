package ras.adlrr.RASBet.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    @Query("SELECT t FROM Transaction t WHERE t.gambler.id = :gambler_id")
    List<Transaction> findAllByGamblerId(@Param("gambler_id") int gambler_id);

    @Query("SELECT t FROM Transaction t WHERE t.gambler.id = :gambler_id")
    List<Transaction> findAllByGamblerId(@Param("gambler_id") int gambler_id, Sort sort);
}

package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ras.adlrr.RASBet.model.Wallet;

import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet,Integer> {
    @Query("SELECT w.gambler.id FROM Wallet w WHERE w.id = :wallet_id")
    Optional<Integer> findGamblerIdByWalletId(@Param("wallet_id") int wallet_id);

    @Query("SELECT w FROM Wallet w WHERE w.gambler.id = :gambler_id")
    List<Wallet> findAllByGamblerId(int gambler_id);
}
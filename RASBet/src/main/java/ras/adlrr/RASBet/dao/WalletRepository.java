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

    @Query("SELECT w.coin.id FROM Wallet w WHERE w.id = :wallet_id")
    // Returns the coin identification of coin used by the wallet with the given id
    String getCoinIdFromWallet(@Param("wallet_id") int wallet_id);

    @Query("SELECT w FROM Wallet w WHERE w.gambler.id = :gambler_id")
    List<Wallet> findAllByGamblerId(@Param("gambler_id") int gambler_id);

    @Query("SELECT w FROM Wallet w WHERE w.gambler.id = :gambler_id AND w.coin.id = :coin_id")
    Wallet getByGamblerIdAndCoinId(@Param("gambler_id") int gambler_id, @Param("coin_id") String coin_id);

    @Query(value = "SELECT CASE WHEN EXISTS(SELECT * FROM wallet WHERE wallet.coin_id = :coin_id AND wallet.gambler_id = :gambler_id) = 1 THEN 'true' ELSE 'false' END", nativeQuery = true)
    boolean hasWalletWithCoin(@Param("gambler_id") int gambler_id, @Param("coin_id") String coin_id);
}
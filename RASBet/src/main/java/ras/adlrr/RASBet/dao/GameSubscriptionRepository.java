package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ras.adlrr.RASBet.model.GameSubscription;

import java.util.List;

public interface GameSubscriptionRepository extends JpaRepository<GameSubscription, GameSubscription.GameSubscriptionID> {
    @Query(value = "SELECT gs.game_id FROM game_subscriptions gs WHERE gs.gambler_id = :gamblerId", nativeQuery = true)
    List<Integer> findAllGamesSubscribedByGambler(@Param("gamblerId") int gamblerId);

    @Query(value = "SELECT gs.gambler_id FROM game_subscriptions gs WHERE gs.game_id = :game_id", nativeQuery = true)
    List<Integer> findAllGameSubscribers(@Param("game_id") int game_id);

    @Modifying
    @Query("DELETE FROM GameSubscription gs WHERE gs.id.game_id = :game_id")
    void deleteAllGameSubscriptions(@Param("game_id") int game_id);
}

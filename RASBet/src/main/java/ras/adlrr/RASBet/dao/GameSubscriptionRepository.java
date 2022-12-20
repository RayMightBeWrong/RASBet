package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ras.adlrr.RASBet.model.GameSubscription;

import java.util.List;

public interface GameSubscriptionRepository extends JpaRepository<GameSubscription, GameSubscription.GameSubscriptionID> {
    @Query(value = "SELECT gs.game_id FROM game_subscriptions gs WHERE gs.gambler_id = :gamblerId", nativeQuery = true)
    List<Integer> findAllGamesSubscribedByGambler(@Param("gamblerId") int gamblerId);
}

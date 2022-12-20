package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ras.adlrr.RASBet.model.GameNotification;
import ras.adlrr.RASBet.model.GameSubscription;

import java.util.List;

public interface GameNotificationRepository extends JpaRepository<GameNotification, Integer> {
    @Query(value = "SELECT gn FROM GameNotification gn WHERE gn.gambler.id = :gamblerId")
    List<GameNotification> findAllByGamblerId(@Param("gamblerId") int gamblerId);
}

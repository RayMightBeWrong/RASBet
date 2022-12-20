package ras.adlrr.RASBet.service.game_subscription;

import ras.adlrr.RASBet.model.GameNotification;

import java.time.LocalDateTime;
import java.util.List;

public interface IGameNotificationService {
    GameNotification createGameNotification(int gambler_id, String type, String msg, LocalDateTime timestamp);
    List<GameNotification> findAllGameNotificationsByGamblerId(int gamblerId);
}

package ras.adlrr.RASBet.service.game_subscription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.GameNotificationRepository;
import ras.adlrr.RASBet.dao.GameSubscriptionRepository;
import ras.adlrr.RASBet.model.GameNotification;
import ras.adlrr.RASBet.model.GameSubscription;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service("gameNotificationService")
public class GameNotificationSubscriptionService implements IGameSubscriptionService, IGameNotificationService{
    private final GameSubscriptionRepository gameSubscriptionRepository;
    private final GameNotificationRepository gameNotificationRepository;

    @Autowired
    public GameNotificationSubscriptionService(GameSubscriptionRepository gameSubscriptionRepository, GameNotificationRepository gameNotificationRepository) {
        this.gameSubscriptionRepository = gameSubscriptionRepository;
        this.gameNotificationRepository = gameNotificationRepository;
    }


    @Override
    public GameNotification createGameNotification(int gambler_id, String type, String msg, LocalDateTime timestamp) {
        return gameNotificationRepository.save(new GameNotification(gambler_id, type, msg, timestamp));
    }

    @Override
    public List<GameNotification> findAllGameNotificationsByGamblerId(int gamblerId) {
        return gameNotificationRepository.findAllByGamblerId(gamblerId);
    }

    @Override
    public GameSubscription subscribeGame(int gambler_id, int game_id) {
        return gameSubscriptionRepository.save(new GameSubscription(gambler_id, game_id));
    }

    @Override
    public void unsubscribeGame(int gambler_id, int game_id) {
        gameSubscriptionRepository.deleteById(new GameSubscription.GameSubscriptionID(gambler_id, game_id));
    }

    @Override
    public List<Integer> findAllIdsOfGamesSubscribedByGambler(int gamblerId) {
        return gameSubscriptionRepository.findAllGamesSubscribedByGambler(gamblerId);
    }

    @Override
    public List<Integer> findAllGameSubscribers(int game_id) {
        return gameSubscriptionRepository.findAllGameSubscribers(game_id);
    }

    @Override
    public boolean isSubscribedToGame(int gambler_id, int game_id) {
        return gameSubscriptionRepository.existsById(new GameSubscription.GameSubscriptionID(gambler_id, game_id));
    }

    @Override
    public void removeGameSubscribers(int game_id) {
        gameSubscriptionRepository.deleteAllGameSubscriptions(game_id);
    }
}

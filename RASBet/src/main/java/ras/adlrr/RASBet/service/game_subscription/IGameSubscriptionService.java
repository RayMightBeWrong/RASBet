package ras.adlrr.RASBet.service.game_subscription;

import ras.adlrr.RASBet.model.GameSubscription;

import java.util.List;

public interface IGameSubscriptionService {
    GameSubscription subscribeGame(int gambler_id, int game_id);
    void unsubscribeGame(int gambler_id, int game_id);
    List<Integer> findAllIdsOfGamesSubscribedByGambler(int gamblerId);
}

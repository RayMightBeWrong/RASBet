package ras.adlrr.RASBet.model;

public interface IGameSubject {
    void subscribe(int gambler_id, IGameSubscriber gameSubscriber);
    void subscribeGame(int gambler_id, int game_id);
    void unsubscribe(int gambler_id);
    void unsubscribeGame(int gambler_id, int game_id);
    //private void notifySubscribers(int game_id);
}

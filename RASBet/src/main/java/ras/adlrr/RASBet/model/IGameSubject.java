package ras.adlrr.RASBet.model;

public interface IGameSubject {
    void subscribe(int gambler_id, IGameSubscriber gameSubscriber);
    void unsubscribe(int gambler_id);
}

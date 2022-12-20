package ras.adlrr.RASBet.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public class ClientNotifier implements IClientNotifier,IGameSubscriber{

    private int client_id;
    private SseEmitter emitter;

    @Autowired
    @Qualifier("betGameFacade")
    private IGameSubject gameSubject;

    public ClientNotifier(int client_id, SseEmitter emitter) {
        this.client_id = client_id;
        this.emitter = emitter;
    }

    @Override
    public void close() {

    }

    @Override
    public int update() {
        return 0;
    }
}

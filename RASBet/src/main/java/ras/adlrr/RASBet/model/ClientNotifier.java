package ras.adlrr.RASBet.model;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

public class ClientNotifier implements IClientNotifier,IGameSubscriber{

    private final int client_id;
    private final SseEmitter emitter;
    private final IGameSubject gameSubject;

    public ClientNotifier(int client_id, SseEmitter emitter, IGameSubject gameSubject) {
        this.client_id = client_id;
        this.emitter = emitter;
        this.gameSubject = gameSubject;
    }

    @Override
    public void close() {
        gameSubject.unsubscribe(client_id);
    }

    @Override
    public int update(String type, String msg) {
        if(type == null || msg == null) return 0;

        String eventFormatted = new JSONObject().put("type", type)
                                                .put("msg", msg)
                                                .toString();

        try { emitter.send(SseEmitter.event().name(type).data(eventFormatted)); }
        catch (IOException e) {
            gameSubject.unsubscribe(client_id);
            close();
            return 0;
        }

        return 1;
    }
}

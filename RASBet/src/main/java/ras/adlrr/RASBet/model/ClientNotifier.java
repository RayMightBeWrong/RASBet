package ras.adlrr.RASBet.model;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;

public class ClientNotifier implements IClientNotifier,IGameSubscriber{

    private final int client_id;
    private final SseEmitter emitter;
    private final IGameSubject gameSubject;

    public ClientNotifier(int client_id, SseEmitter emitter, IGameSubject gameSubject) {
        this.client_id = client_id;
        this.emitter = emitter;
        this.gameSubject = gameSubject;
        gameSubject.subscribe(client_id, this);
    }

    @Override
    public void close() {
        emitter.complete();
        gameSubject.unsubscribe(client_id);
    }

    @Override
    public int update(String type, String msg, LocalDateTime timestamp) {
        if(type == null || msg == null) return 0;

        String eventFormatted = new JSONObject().put("type", type)
                                                .put("msg", msg)
                                                .put("timestamp", timestamp.toString())
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

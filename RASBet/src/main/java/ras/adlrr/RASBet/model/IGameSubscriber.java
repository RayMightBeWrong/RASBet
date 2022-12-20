package ras.adlrr.RASBet.model;

import java.io.IOException;
import java.time.LocalDateTime;

public interface IGameSubscriber {

    /**
     *
     * @return 1 indicates success. 0 indicates fail.
     */
    int update(String type, String msg, LocalDateTime timestamp);

    /**
     * Used to inform the subscriber that it was unsubscribed.
     */
    void unsubscribed();
}

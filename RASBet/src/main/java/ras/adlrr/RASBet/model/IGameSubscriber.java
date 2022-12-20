package ras.adlrr.RASBet.model;

import java.io.IOException;

public interface IGameSubscriber {

    /**
     *
     * @return 1 indicates success. 0 indicates fail.
     */
    int update(String type, String msg);
}

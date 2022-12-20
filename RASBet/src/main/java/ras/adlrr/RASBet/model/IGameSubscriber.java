package ras.adlrr.RASBet.model;

import java.io.IOException;

public interface IGameSubscriber {

    /**
     *
     * @return if -1 an error occured that invalidates the subscriber. 1 indicates success.
     */
    int update();
}

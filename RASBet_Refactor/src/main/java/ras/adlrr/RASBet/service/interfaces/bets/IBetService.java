package ras.adlrr.RASBet.service.interfaces.bets;

import java.util.List;

import org.springframework.data.domain.Sort;
import ras.adlrr.RASBet.model.Bet;
import ras.adlrr.RASBet.model.Transaction;

public interface IBetService {
    /**
     * Gets bet by id
     * @param id Identification of the bet
     * @return bet associated with the given id, or 'null' if no bet was found with the given id.
     */
    Bet getBet(int id);

    /**
     * Registers bet, and returns the updated version.
     * @param bet Bet to be registered
     * @return Updated bet
     * @throws Exception If there is any restriction that is not met.
     */
    Bet addBet(Bet bet) throws Exception;

    /**
     * Removes bet
     * @param betID Identification of the bet
     * @throws Exception If there was a problem when trying to remove the bet.
     */
    void removeBet(int betID) throws Exception;

    /**
     * @param gambler_id Identification of the gambler that registered the bets.
     * @return Bets registered by a specific gambler.
     * @throws Exception If the gambler does not exists, or any problem occurs when trying to perform the get operation.
     */
    List<Bet> getGamblerBets(int gambler_id) throws Exception;

    /**
     * @param gambler_id Identification of the gambler that registered the bets.
     * @param direction Defines the direction of the sort, ascending or descending.
     * @return Bets registered by a specific gambler, sorted by registration date.
     * @throws Exception If the gambler does not exists, or any problem occurs when trying to perform the get operation.
     */
    List<Bet> getGamblerBets(int gambler_id, Sort.Direction direction) throws Exception;

    /**
     * Closes bet
     * @param bet_id Identification of the bet
     * @return Winnings of the bet
     * @throws Exception If any problem occurs when trying to close the bet.
     */
    float closeBet(int bet_id) throws Exception;

    /**
     * Gets all the bets registered for the specific game.
     * @param game_id Identification of the game
     * @return all the bets registered for the specific game.
     * @throws Exception If any problem occurs when trying to close the bet.
     */
    List<Bet> getBetsByGameId(int game_id) throws Exception;

    /**
     * Gets all ids of the bets registered for the specific game.
     * @param game_id Identification of the game
     * @return all ids of the bets registered for the specific game.
     * @throws Exception If any problem occurs when trying to close the bet.
     */
    List<Integer> getBetsIdsByGameId(int game_id) throws Exception;
}

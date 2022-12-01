package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ras.adlrr.RASBet.model.GameChoice;

import java.util.List;

public interface GameChoiceRepository extends JpaRepository<GameChoice,Integer> {

    //@Query(value = "SELECT gc.* FROM (SELECT * FROM transactions WHERE transactions.gambler_id = :gambler_id) AS t " +
    //               "INNER JOIN bets b ON t.id = b.id " +
    //               "INNER JOIN game_choices gc ON b.id = gc.bet_id", nativeQuery = true)
    //List<GameChoice> findGamblerGameChoices(@Param("gambler_id") int gambler_id);

    /**
     * @param gambler_id Identification of the gambler
     * @return ids of the game choices made by the gambler
     */
    @Query(value = "SELECT gc.id FROM ((SELECT id FROM transactions WHERE transactions.gambler_id = :gambler_id) as t " +
            "INNER JOIN (SELECT id FROM bets) AS b on t.id = b.id) " +
            "INNER JOIN game_choices gc on b.id = gc.bet_id;", nativeQuery = true)
    List<Integer> findGamblerGameChoicesIds(@Param("gambler_id") int gambler_id);

}

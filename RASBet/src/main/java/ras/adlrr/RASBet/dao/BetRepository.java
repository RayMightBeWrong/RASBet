package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Bet;
import ras.adlrr.RASBet.model.GameChoice;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet, Integer> {
    @Query("SELECT b FROM Bet b WHERE b.transaction.gambler.id = :gambler_id")
    List<Bet> findAllByGamblerId(@Param("gambler_id") int gambler_id);

    @Query("select gc.* from transactions as t inner join bets b on t.id = b.id" +
            "                                  inner join game_choices gc on b.id = gc.bet_id" +
            "                                  where t.gambler_id = :gambler_id;")
    List<GameChoice> findGamblerGameChoices(@Param("gambler_id") int gambler_id);
}

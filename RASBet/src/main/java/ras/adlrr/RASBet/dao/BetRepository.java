package ras.adlrr.RASBet.dao;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Bet;

import java.util.List;

@Repository
public interface BetRepository extends JpaRepository<Bet, Integer> {
    @Query("SELECT b FROM Bet b WHERE b.transaction.gambler.id = :gambler_id")
    List<Bet> findAllByGamblerId(@Param("gambler_id") int gambler_id);

    default List<Bet> findAllByGamblerIdSortByDate(@Param("gambler_id") int gambler_id, Sort.Direction direction) throws Exception {
        if(direction.isAscending())
            return findAllByGamblerIdSortAscendingByDate(gambler_id);
        else if(direction.isDescending())
            return findAllByGamblerIdSortDescendingByDate(gambler_id);
        else
            throw new Exception("Invalid sort direction.");
    }

    @Query("SELECT b FROM Bet b WHERE b.transaction.gambler.id = :gambler_id " +
                               "ORDER BY b.transaction.date ASC")
    List<Bet> findAllByGamblerIdSortAscendingByDate(@Param("gambler_id") int gambler_id);

    @Query("SELECT b FROM Bet b WHERE b.transaction.gambler.id = :gambler_id " +
            "ORDER BY b.transaction.date ASC")
    List<Bet> findAllByGamblerIdSortDescendingByDate(@Param("gambler_id") int gambler_id);

    @Query(value = "SELECT gc.bet_id FROM game_choices gc WHERE gc.game_id = :game_id", nativeQuery = true)
    List<Integer> getBetsIdsByGameId(@Param("game_id") int game_id);

}

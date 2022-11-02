package ras.adlrr.RASBet.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Sport;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    Optional<Game> findByExtID(String extID);
    @Query("SELECT g FROM Game g WHERE g.sport.id = :sport_id")
    List<Game> findAllBySportId(@Param("sport_id") String sport_id);

    //Get game with participants
    @Query("SELECT g FROM Game g LEFT JOIN FETCH g.participants WHERE g.id = :game_id")
    Optional<Game> loadGameById(@Param("game_id") int game_id);
}

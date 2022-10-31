package ras.adlrr.RASBet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Game;

@Repository
public interface GameRepository extends JpaRepository<Game, Integer> {
    List<Game> findAllByExtID(String extID);
}

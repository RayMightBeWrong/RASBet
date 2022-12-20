package ras.adlrr.RASBet.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ras.adlrr.RASBet.model.Participant;

import java.util.Set;

public interface ParticipantRepository extends JpaRepository<Participant,Integer> {
    @Query("SELECT g.participants FROM Game g WHERE g.id = :game_id")
    Set<Participant> findAllByGameId(@Param("game_id") int game_id);

    @Query(value = "SELECT p.game_id FROM participants p WHERE p.id = :participant_id", nativeQuery = true)
    int getGameID(@Param("participant_id") int participant_id);
}

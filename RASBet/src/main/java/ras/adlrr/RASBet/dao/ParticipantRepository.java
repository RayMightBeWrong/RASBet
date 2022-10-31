package ras.adlrr.RASBet.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Participant;

public interface ParticipantRepository extends JpaRepository<Participant,Integer> {
    List<Participant> findAllByGame(Game game);
}

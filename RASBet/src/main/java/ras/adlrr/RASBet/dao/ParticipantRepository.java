package ras.adlrr.RASBet.dao;


import org.springframework.data.jpa.repository.JpaRepository;

import ras.adlrr.RASBet.model.Participant;

public interface ParticipantRepository extends JpaRepository<Participant,Integer> {
}

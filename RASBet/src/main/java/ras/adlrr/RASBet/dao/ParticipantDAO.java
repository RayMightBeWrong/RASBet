package ras.adlrr.RASBet.dao;

import ras.adlrr.RASBet.model.Participant;

import java.util.List;

public interface ParticipantDAO {
    Participant getParticipant(int id);
    List<Participant> getGameParticipants(int gameID);
    int addParticipantToGame(int gameID, Participant p);
}

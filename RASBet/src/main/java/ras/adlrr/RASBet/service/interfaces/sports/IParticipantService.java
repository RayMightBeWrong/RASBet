package ras.adlrr.RASBet.service.interfaces.sports;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import ras.adlrr.RASBet.model.GameChoice;
import ras.adlrr.RASBet.model.Participant;

public interface IParticipantService {
    Set<Participant> getGameParticipants(int gameID) throws Exception;

    void addParticipantsToGame(int gameID, Collection<Participant> participants) throws Exception;

    void addParticipantToGame(int gameID, Participant p) throws Exception;

    void removeParticipant(int participant_id) throws Exception;

    Participant getParticipant(int participant_id);

    void editOddInParticipant(int participant_id, float odd) throws Exception;

    void editScoreInParticipant(int participant_id, int score) throws Exception;

    boolean participantExistsById(int id);

    void giveOddToGameChoice(GameChoice gc);

    void giveOddToGameChoices(List<GameChoice> gcs);

    int getGameID(int participant_id);
}

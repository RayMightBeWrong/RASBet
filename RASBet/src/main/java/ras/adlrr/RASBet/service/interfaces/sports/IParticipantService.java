package ras.adlrr.RASBet.service.interfaces.sports;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import ras.adlrr.RASBet.model.GameChoice;
import ras.adlrr.RASBet.model.Participant;

public interface IParticipantService {
    public Set<Participant> getGameParticipants(int gameID) throws Exception;

    public void addParticipantsToGame(int gameID, Collection<Participant> participants) throws Exception;

    public void addParticipantToGame(int gameID, Participant p) throws Exception;

    public void removeParticipant(int participant_id) throws Exception;

    public Participant getParticipant(int participant_id);

    public void editOddInParticipant(int participant_id, float odd) throws Exception;

    public void editScoreInParticipant(int participant_id, int score) throws Exception;

    public boolean participantExistsById(int id);

    public void giveOddToGameChoice(GameChoice gc);

    public void giveOddToGameChoices(List<GameChoice> gcs);
}

package ras.adlrr.RASBet.dao;

import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Participant;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository("FakeParticipantDAO")
public class FakeParticipantDataAccessService implements ParticipantDAO{

    //Key: gameID
    private static HashMap<Integer, List<Participant>> mapOfParticipants = new HashMap<>();

    @Override
    public List<Participant> getGameParticipants(int gameID) {
        return mapOfParticipants.get(gameID).stream().map(Participant::new).toList();
    }

    @Override
    public int addParticipantToGame(int gameID, Participant p) {
        var l = mapOfParticipants.get(gameID);

        if (l == null){
            l = new ArrayList<>();
            mapOfParticipants.put(gameID, l);
        }

        l.add(new Participant(p));
        return 1;
    }
}

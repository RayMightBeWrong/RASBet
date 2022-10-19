package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.GameDAO;
import ras.adlrr.RASBet.dao.ParticipantDAO;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Participant;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class GameService {
    private final GameDAO gameDAO;
    private final ParticipantDAO participantDAO;

    /* **** Game Methods **** */

    @Autowired
    public GameService(@Qualifier("FakeGameDAO") GameDAO gameDAO, @Qualifier("FakeParticipantDAO") ParticipantDAO participantDAO){
        this.gameDAO = gameDAO;
        this.participantDAO = participantDAO;
    }

    public List<Game> getGames() {
        return gameDAO.getGames();
    }

    public Game getGame(int id){
        return gameDAO.getGame(id);
    }

    public int addGame(Game g) {
        return gameDAO.addGame(g);
    }

    public int changeGameState(int id, String state){
        return gameDAO.changeGameState(id, state);
    }

    public int changeGameDate(int id, LocalDateTime date) {
        return gameDAO.changeGameDate(id,date);
    }


    /* **** Participants Methods **** */

    public List<Participant> getGameParticipants(int gameID) {
        return participantDAO.getGameParticipants(gameID);
    }

    public int addParticipantToGame(int gameID, Participant p) {
        return participantDAO.addParticipantToGame(gameID, p);
    }
}

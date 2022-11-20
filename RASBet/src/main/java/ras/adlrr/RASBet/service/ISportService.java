package ras.adlrr.RASBet.service;

import java.util.List;

import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Sport;

public interface ISportService {
    public Sport addSport(Sport sport) throws Exception;

    public Sport findSportById(String id);

    public void removeSport(String id) throws Exception;

    public List<Sport> getListOfSports();

    public List<Game> getGamesFromSport(String sport) throws Exception
    ;
    public boolean sportExistsById(String id);
}

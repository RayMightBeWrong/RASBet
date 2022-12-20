package ras.adlrr.RASBet.service.interfaces.sports;

import java.util.List;

import ras.adlrr.RASBet.model.Sport;

public interface ISportService {
    Sport addSport(Sport sport) throws Exception;

    Sport findSportById(String id);

    void removeSport(String id) throws Exception;

    List<Sport> getListOfSports();
    
    boolean sportExistsById(String id);
}

package ras.adlrr.RASBet.dao;

import ras.adlrr.RASBet.model.Sport;

import java.util.List;

public interface SportDAO {
    Sport getSport(int id);
    int addSport(Sport sport);

    int removeSport(int id);

    List<Sport> getListOfSports();
}

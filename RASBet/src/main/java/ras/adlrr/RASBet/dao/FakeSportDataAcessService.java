package ras.adlrr.RASBet.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ras.adlrr.RASBet.model.Sport;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Repository("fakeSportDAO")
public class FakeSportDataAcessService implements SportDAO{

    private final static HashMap<Integer,Sport> DB = new HashMap<>();
    private int id = 0;

    @Override
    public Sport getSport(int id) {
        return DB.get(id);
    }

    @Override
    public int addSport(Sport sport) {
        sport.setId(id);
        DB.put(id, sport);
        id++;
        return 1;
    }

    @Override
    public int removeSport(int id) {
        DB.remove(id);
        return 1;
    }

    @Override
    public List<Sport> getListOfSports() {
        return DB.values().stream().toList();
    }
}

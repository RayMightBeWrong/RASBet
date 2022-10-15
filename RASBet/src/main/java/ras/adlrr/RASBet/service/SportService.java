package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.FakeSportDataAcessService;
import ras.adlrr.RASBet.dao.SportDAO;
import ras.adlrr.RASBet.model.Sport;

import java.util.List;

@Service
public class SportService {
    private final SportDAO sportDAO;

    @Autowired
    public SportService(@Qualifier("fakeSportDAO") SportDAO sportDAO){
        this.sportDAO = sportDAO;
    }

    public int addSport(Sport sport){
        return sportDAO.addSport(sport);
    }

    public Sport getSport(int id){
        return sportDAO.getSport(id);
    }

    public int removeSport(int id) {
        return sportDAO.removeSport(id);
    }

    public List<Sport> getListOfSports() {
        return sportDAO.getListOfSports();
    }

}

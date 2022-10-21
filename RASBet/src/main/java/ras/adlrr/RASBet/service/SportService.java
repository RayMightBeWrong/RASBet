package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.SportDAO;
import ras.adlrr.RASBet.dao.SportRepository;
import ras.adlrr.RASBet.model.Sport;

import java.util.List;

@Service
public class SportService {
    /*
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
    */
    @Autowired
    private final SportRepository sportRepository;

    @Autowired
    public SportService(SportRepository sportRepository){
        this.sportRepository = sportRepository;
    }

    public int addSport(Sport sport){
        sportRepository.save(sport);
        return 1;
    }

    public Sport getSport(int id){
        return sportRepository.findById(id).orElse(null);
    }

    public int removeSport(int id) {
        sportRepository.deleteById(id);
        return 1;
    }

    public List<Sport> getListOfSports() {
        return sportRepository.findAll();
    }

}

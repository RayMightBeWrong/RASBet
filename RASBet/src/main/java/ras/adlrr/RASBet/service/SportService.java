package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ras.adlrr.RASBet.dao.SportRepository;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Sport;

import java.util.List;

@Service
public class SportService {
    @Autowired
    private final SportRepository sportRepository;

    @Autowired
    public SportService(SportRepository sportRepository){
        this.sportRepository = sportRepository;
    }

    public Sport addSport(Sport sport){
        // TODO: validate method
        sportRepository.save(sport);
        return sport;
    }

    public Sport getSport(int id){
        return sportRepository.findById(id).orElse(null);
    }

    public Sport removeSport(int id) {
        Sport s = sportRepository.findById(id).orElse(null);
        if(s != null)
            sportRepository.deleteById(id);
        return s;
    }

    public List<Sport> getListOfSports() {
        return sportRepository.findAll();
    }

}

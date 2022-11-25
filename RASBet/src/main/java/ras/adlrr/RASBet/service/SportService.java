package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ras.adlrr.RASBet.dao.GameRepository;
import ras.adlrr.RASBet.dao.SportRepository;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Sport;

import java.util.List;

@Service
public class SportService implements ISportService{
    private final SportRepository sportRepository;
    private final GameRepository gameRepository;

    @Autowired
    public SportService(SportRepository sportRepository, GameRepository gameRepository){
        this.sportRepository = sportRepository;
        this.gameRepository = gameRepository;
    }


    /**
     * Adds a sport to the repository
     * @param sport Sport to be persisted
     * @return sport updated by the repository
     * @throws Exception If any of the attributes does not meet the requirements an Exception is thrown indicating the error.
     */
    public Sport addSport(Sport sport) throws Exception {
        if(sportRepository.existsById(sport.getId()))
            throw new Exception("Sport already exists!");

        sportRepository.save(sport);
        
        return sport;
    }

    /**
     * Checks for the existence of a sport with the given id. If the sport exists, returns it.
     * @param id Identification of the sport
     * @return sport if it exists, or null
     */
    public Sport findSportById(String id) {
        return sportRepository.findById(id).orElse(null);
    }

    /**
     * If a sport with the given id exists, removes it from the repository
     * @param id Identification of the sport
     * @throws Exception If the sport does not exist.
     */
    public void removeSport(String id) throws Exception {
        if(!sportRepository.existsById(id))
            throw new Exception("Sport needs to exist to be removed!");

        sportRepository.deleteById(id);
    }

    /**
     * @return list of sports present in the repository
     */
    public List<Sport> getListOfSports() {
        return sportRepository.findAll();
    }

    /**
     * @param sport Identification of the sport
     * @return list of games of a sport present in the repository
     */
    public List<Game> getGamesFromSport(String sport) throws Exception {
        Sport s = sportRepository.findById(sport).orElse(null);
        if(s == null)
            throw new Exception("Sport not found!");

        return gameRepository.findAllBySportId(s.getId());
    }

    /**
     * Checks for the existence of a sport with the given id
     * @param id Identification of the sport
     * @return true if a sport exists with the given identification
     */
    public boolean sportExistsById(String id) {
        return sportRepository.existsById(id);
    }
}

package ras.adlrr.RASBet.service.sports;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.SportRepository;
import ras.adlrr.RASBet.model.Sport;
import ras.adlrr.RASBet.service.interfaces.sports.ISportService;

import java.util.List;

@Service("sportService")
@RequiredArgsConstructor
public class SportService implements ISportService {
    private final SportRepository sportRepository;

    /**
     * Adds a sport to the repository
     * @param sport Sport to be persisted
     * @return sport updated by the repository
     * @throws Exception If any of the attributes does not meet the requirements an Exception is thrown indicating the error.
     */
    public Sport addSport(Sport sport) throws Exception {
        if(sportRepository.existsById(sport.getId()))
            throw new Exception("Sport already exists!");
        if(sport.getType() != Sport.WITH_DRAW &&
                sport.getType() != Sport.WITHOUT_DRAW &&
                sport.getType() != Sport.RACE)
            throw new Exception("Not a valid type of sport.");
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
     * Checks for the existence of a sport with the given id
     * @param id Identification of the sport
     * @return true if a sport exists with the given identification
     */
    public boolean sportExistsById(String id) {
        return sportRepository.existsById(id);
    }
}

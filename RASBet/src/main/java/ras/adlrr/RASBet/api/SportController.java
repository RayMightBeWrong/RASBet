package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ras.adlrr.RASBet.dao.SportRepository;
import ras.adlrr.RASBet.model.Sport;
import ras.adlrr.RASBet.service.SportService;

import java.util.List;

@RequestMapping("/api/sports/")
@RestController
public class SportController {
    private final SportService sportService;

    @Autowired
    public SportController(SportService sportService){
        this.sportService = sportService;
    }

    @PostMapping
    public ResponseEntity<Sport> addSport(@RequestBody Sport sport){
        sport = sportService.addSport(sport);
        if(sport != null)
            return new ResponseEntity<>(sport, HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "{id}")
    public Sport getSport(@PathVariable("id") int id){
        return sportService.getSport(id);
    }

    @DeleteMapping(path = "{id}")
    public Sport removeSport(@PathVariable int id) {
        return sportService.removeSport(id);
    }

    @GetMapping
    public List<Sport> getListOfSports() {
        return sportService.getListOfSports();
    }
}

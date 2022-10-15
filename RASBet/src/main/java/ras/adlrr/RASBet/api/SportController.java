package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ras.adlrr.RASBet.dao.SportDAO;
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
    public int addSport(@RequestBody Sport sport){
        return sportService.addSport(sport);
    }

    @GetMapping(path = "{id}")
    public Sport getSport(@PathVariable("id") int id){
        return sportService.getSport(id);
    }

    @DeleteMapping(path = "{id}")
    public int removeSport(@PathVariable int id) {
        return sportService.removeSport(id);
    }

    @GetMapping
    public List<Sport> getListOfSports() {
        return sportService.getListOfSports();
    }
}

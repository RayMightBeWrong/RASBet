package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ras.adlrr.RASBet.api.auxiliar.ResponseEntityBadRequest;
import ras.adlrr.RASBet.model.Game;
import ras.adlrr.RASBet.model.Sport;
import ras.adlrr.RASBet.service.ISportService;
import ras.adlrr.RASBet.service.SportService;

import java.util.List;

@RequestMapping("/api/sports")
@RestController
public class SportController {
    private final ISportService sportService;

    @Autowired
    public SportController(ISportService sportService){
        this.sportService = sportService;
    }

    @PostMapping
    public ResponseEntity<Sport> addSport(@RequestBody Sport sport){
        try{ 
            return ResponseEntity.ok().body(sportService.addSport(sport));
        }
        catch (Exception e){
            return new ResponseEntityBadRequest<Sport>().createBadRequest(e.getMessage());
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Sport> getSport(@PathVariable("id") String id){
        return new ResponseEntity<>(sportService.findSportById(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity removeSport(@PathVariable String id) {
        try {
            sportService.removeSport(id);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Sport>> getListOfSports() {
        return new ResponseEntity<>(sportService.getListOfSports(),HttpStatus.OK);
    }

    @GetMapping("/{id}/games")
    public ResponseEntity<List<Game>> getGamesFromSport(@PathVariable("id") String id) {
        try{
            return ResponseEntity.ok().body(sportService.getGamesFromSport(id));
        }
        catch (Exception e){
            return new ResponseEntityBadRequest<List<Game>>().createBadRequest(e.getMessage());
        }
    }
}

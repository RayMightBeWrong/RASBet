package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ras.adlrr.RASBet.model.Sport;
import ras.adlrr.RASBet.service.SportService;

import java.util.AbstractMap;
import java.util.List;

@RequestMapping("/api/sports")
@RestController
public class SportController {
    private final SportService sportService;

    @Autowired
    public SportController(SportService sportService){
        this.sportService = sportService;
    }

    @PostMapping
    public ResponseEntity addSport(@RequestBody Sport sport){
        try{ return ResponseEntity.ok().body(sportService.addSport(sport)); }
        catch (Exception e){
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("error",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Sport> getSport(@PathVariable("id") int id){
        return new ResponseEntity<>(sportService.getSport(id), HttpStatus.OK);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity removeSport(@PathVariable int id) {

        try {
            sportService.removeSport(id);
            return new ResponseEntity(HttpStatus.OK); }
        catch (Exception e){
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("error",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Sport>> getListOfSports() {
        return new ResponseEntity<>(sportService.getListOfSports(),HttpStatus.OK);
    }

    @GetMapping("/{sport_name}/games")
    public ResponseEntity getGamesFromSport(@PathVariable("sport_name") String sport_name) {
        try{
            return ResponseEntity.ok().body(sportService.getGamesFromSport(sport_name));
        }catch (Exception e){
            return ResponseEntity.badRequest().body(new AbstractMap.SimpleEntry<>("error",e.getMessage()));
        }
    }

}

package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ras.adlrr.RASBet.model.Bet;
import ras.adlrr.RASBet.service.BetService;

import java.util.AbstractMap;

@RequestMapping("/api/bets")
@RestController
public class BetController {
    private final BetService betService;

    @Autowired
    public BetController(BetService betService) {
        this.betService = betService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Bet> getBet(@PathVariable("id") int id) {
        return ResponseEntity.ok().body(betService.getBet(id));
    }

    @PostMapping
    public ResponseEntity addBet(@RequestBody Bet bet) {
        try{ return ResponseEntity.ok().body(betService.addBet(bet)); }
        catch (Exception e){
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("error",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/gambler/{id}")
    public ResponseEntity getGamblerBets(@PathVariable("id") int userID) {
        try{ return ResponseEntity.ok().body(betService.getGamblerBets(userID)); }
        catch (Exception e){
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("error",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity removeBet(@PathVariable("id") int betID) {
        try {
            betService.removeBet(betID);
            return new ResponseEntity(HttpStatus.OK); }
        catch (Exception e){
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("error",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}

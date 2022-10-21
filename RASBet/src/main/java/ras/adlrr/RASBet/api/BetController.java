package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ras.adlrr.RASBet.model.Bet;
import ras.adlrr.RASBet.service.BetService;

import java.util.List;

@RequestMapping("/api/bets/")
@RestController
public class BetController {
    private final BetService betService;

    @Autowired
    public BetController(BetService betService) {
        this.betService = betService;
    }

    @GetMapping(path = "{id}")
    public Bet getBet(@PathVariable("id") int id) {
        return betService.getBet(id);
    }

    @PostMapping
    public int addBet(@RequestBody Bet bet) {
        return betService.addBet(bet);
        /*
        int r = betService.addBet(bet);
        if(r == -1)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(r);
        else
            return ResponseEntity.status(HttpStatus.OK).body(r);
        */
    }

    @GetMapping(path = "/user/{id}")
    public List<Bet> getUserBets(@PathVariable("id") int userID) {
        return betService.getUserBets(userID);
    }

    @DeleteMapping(path = "{id}")
    public void removeBet(@PathVariable("id") int betID) {
        betService.removeBet(betID);
    }
}

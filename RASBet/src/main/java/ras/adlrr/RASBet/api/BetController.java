package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
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
    }

    @GetMapping(path = "/user/{id}")
    public List<Bet> getUserBets(@PathVariable("id") int userID) {
        return betService.getUserBets(userID);
    }

    @DeleteMapping(path = "{id}")
    public int removeBet(@PathVariable("id") int betID) {
        return betService.removeBet(betID);
    }
}

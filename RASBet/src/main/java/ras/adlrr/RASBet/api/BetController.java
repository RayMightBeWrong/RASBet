package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ras.adlrr.RASBet.api.auxiliar.ResponseEntityBadRequest;
import ras.adlrr.RASBet.model.Bet;
import ras.adlrr.RASBet.model.Transaction;
import ras.adlrr.RASBet.service.IBetService;

import java.util.List;

@RequestMapping("/api/bets")
@RestController
public class BetController {
    private final IBetService betService;

    @Autowired
    public BetController(IBetService betService) {
        this.betService = betService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Bet> getBet(@PathVariable("id") int id) {
        return ResponseEntity.ok().body(betService.getBet(id));
    }

    @PostMapping
    public ResponseEntity<Bet> addBet(@RequestBody Bet bet) {
        try{ return ResponseEntity.ok().body(betService.addBet(bet)); }
        catch (Exception e){
            return new ResponseEntityBadRequest<Bet>().createBadRequest(e.getMessage());
        }
    }

    @PutMapping(path = "/withdraw")
    public ResponseEntity<Transaction> withdrawBetWinnings(@RequestParam("bet_id") int bet_id, @RequestParam("wallet_id") int wallet_id){
        try {
            return ResponseEntity.ok().body(betService.withdrawBetWinnings(bet_id, wallet_id));
        }catch (Exception e){
            return new ResponseEntityBadRequest<Transaction>().createBadRequest(e.getMessage());
        }
    }

    @GetMapping(path = "/gambler/{id}")
    public ResponseEntity<List<Bet>> getGamblerBets(@PathVariable("id") int userID) {
        try{ return ResponseEntity.ok().body(betService.getGamblerBets(userID)); }
        catch (Exception e){
            return new ResponseEntityBadRequest<List<Bet>>().createBadRequest(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity removeBet(@PathVariable("id") int betID) {
        try {
            betService.removeBet(betID);
            return new ResponseEntity(HttpStatus.OK); }
        catch (Exception e){
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
        }
    }
}
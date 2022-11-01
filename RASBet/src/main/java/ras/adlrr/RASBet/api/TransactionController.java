package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ras.adlrr.RASBet.model.Transaction;
import ras.adlrr.RASBet.service.TransactionService;

import java.util.AbstractMap;
import java.util.List;

@RequestMapping("/api/transactions")
@RestController
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService t){
        this.transactionService = t;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable int id) {
        return ResponseEntity.ok().body(transactionService.getTransaction(id));
    }

    @PostMapping
    public ResponseEntity addTransaction(@RequestBody Transaction t) {
        try {
            return ResponseEntity.ok().body(transactionService.addTransaction(t));
        }catch (Exception e){
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("error",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/gambler/{id}")
    public ResponseEntity getUserTransactions(@PathVariable("id") int gambler_id) {
        try {
            return ResponseEntity.ok().body(transactionService.getGamblerTransactions(gambler_id));
        }catch (Exception e){
            return new ResponseEntity(new AbstractMap.SimpleEntry<>("error",e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(){
        return ResponseEntity.ok().body(transactionService.getTransactions());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity removeTransaction(@PathVariable int id) {
        try {
            transactionService.removeTransaction(id);
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }
}

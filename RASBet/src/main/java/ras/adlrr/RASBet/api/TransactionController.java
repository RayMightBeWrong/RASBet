package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ras.adlrr.RASBet.api.auxiliar.ResponseEntityBadRequest;
import ras.adlrr.RASBet.model.Transaction;
import ras.adlrr.RASBet.model.Wallet;
import ras.adlrr.RASBet.service.TransactionService;

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

    @GetMapping(path = "/gambler/{id}")
    public ResponseEntity<List<Transaction>> getUserTransactions(@PathVariable("id") int gambler_id) {
        try {
            return ResponseEntity.ok().body(transactionService.getGamblerTransactions(gambler_id));
        }catch (Exception e){
            return new ResponseEntityBadRequest<List<Transaction>>().createBadRequest(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Transaction>> getTransactions(){
        return ResponseEntity.ok().body(transactionService.getTransactions());
    }

    @PostMapping
    public ResponseEntity<Transaction> addTransaction(@RequestBody Transaction t) {
        try {
            return ResponseEntity.ok().body(transactionService.addTransaction(t));
        }catch (Exception e){
            return new ResponseEntityBadRequest<Transaction>().createBadRequest(e.getMessage());
        }
    }

    @PutMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestParam int wallet_id, @RequestParam float value){
        try {
            return ResponseEntity.ok().body(transactionService.deposit(wallet_id, value));
        }catch (Exception e){
            return new ResponseEntityBadRequest<Transaction>().createBadRequest(e.getMessage());
        }
    }

    @PutMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestParam int wallet_id, @RequestParam float value){
        try {
            return ResponseEntity.ok().body(transactionService.withdraw(wallet_id, value));
        }catch (Exception e){
            return new ResponseEntityBadRequest<Transaction>().createBadRequest(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity removeTransaction(@PathVariable int id) {
        try {
            transactionService.removeTransaction(id);
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
        }
    }
}
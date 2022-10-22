package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ras.adlrr.RASBet.model.Transaction;
import ras.adlrr.RASBet.service.TransactionService;

import java.util.List;

@RequestMapping("/api/transactions/")
@RestController
public class TransactionController {
    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService t){
        this.transactionService = t;
    }

    @GetMapping(path = "{id}")
    public Transaction getTransaction(@PathVariable int id) {
        return transactionService.getTransaction(id);
    }

    @PostMapping
    public int addTransaction(@RequestBody Transaction t) {
        return transactionService.addTransaction(t);
    }

    @GetMapping(path = "/user/{id}")
    public List<Transaction> getUserTransactions(@PathVariable("id") int userID) {
        return transactionService.getUserTransactions(userID);
    }

    @GetMapping
    public List<Transaction> getTransactions(){
        return transactionService.getTransactions();
    }

    @DeleteMapping(path = "{id}")
    public int removeTransaction(@PathVariable int id) {
        return transactionService.removeTransaction(id);
    }
}

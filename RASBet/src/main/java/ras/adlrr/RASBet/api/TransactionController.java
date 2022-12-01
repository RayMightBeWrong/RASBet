package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ras.adlrr.RASBet.api.auxiliar.ResponseEntityBadRequest;
import ras.adlrr.RASBet.model.Transaction;
import ras.adlrr.RASBet.service.interfaces.IUserTransactions;
import ras.adlrr.RASBet.service.interfaces.transactions.ITransactionService;

import java.util.List;

@RequestMapping("/api/transactions")
@RestController
@CrossOrigin
public class TransactionController {
    private final ITransactionService transactionService;
    private final IUserTransactions userTransactions;

    @Autowired
    public TransactionController(@Qualifier("transactionFacade") ITransactionService transactionService,
                                 @Qualifier("transactionFacade") IUserTransactions userTransactions){
        this.transactionService = transactionService;
        this.userTransactions = userTransactions;
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

    @GetMapping(path = "/gambler/{id}/{direction}")
    public ResponseEntity<List<Transaction>> getUserTransactions(@PathVariable("id") int gambler_id, @PathVariable("direction") String direction) {
        try {
            Sort.Direction sortDirection;
            if(direction.equals("ASC"))
                sortDirection = Sort.Direction.ASC;
            else if (direction.equals("DESC"))
                sortDirection = Sort.Direction.DESC;
            else
                throw new Exception("Sort direction must be \"ASC\" or \"DESC\"");

            return ResponseEntity.ok().body(transactionService.getGamblerTransactions(gambler_id, sortDirection));
        }catch (Exception e){
            return new ResponseEntityBadRequest<List<Transaction>>().createBadRequest(e.getMessage());
        }
    }

    @PutMapping("/deposit")
    public ResponseEntity<Transaction> deposit(@RequestParam int wallet_id, @RequestParam float value){
        try {
            return ResponseEntity.ok().body(userTransactions.deposit(wallet_id, value));
        }catch (Exception e){
            return new ResponseEntityBadRequest<Transaction>().createBadRequest(e.getMessage());
        }
    }

    @PutMapping("/withdraw")
    public ResponseEntity<Transaction> withdraw(@RequestParam int wallet_id, @RequestParam float value){
        try {
            return ResponseEntity.ok().body(userTransactions.withdraw(wallet_id, value));
        }catch (Exception e){
            return new ResponseEntityBadRequest<Transaction>().createBadRequest(e.getMessage());
        }
    }

    @PutMapping(path = "/claimBalancePromotion")
    public ResponseEntity claimBalancePromotion(@RequestParam("gambler_id") int gambler_id, @RequestParam("coupon") String coupon) {
        try {
            userTransactions.claimBalancePromotion(gambler_id, coupon);
            return new ResponseEntity(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
        }
    }
}
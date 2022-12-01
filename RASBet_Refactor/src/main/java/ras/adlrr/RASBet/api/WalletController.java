package ras.adlrr.RASBet.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ras.adlrr.RASBet.api.auxiliar.ResponseEntityBadRequest;
import ras.adlrr.RASBet.model.Coin;
import ras.adlrr.RASBet.model.Wallet;
import ras.adlrr.RASBet.service.interfaces.balance.ICoinService;
import ras.adlrr.RASBet.service.interfaces.balance.IWalletService;

@RequestMapping("/api/wallets")
@RestController
@CrossOrigin
public class WalletController {
    private final IWalletService walletService;
    private final ICoinService coinService;

    @Autowired
    public WalletController(@Qualifier("balanceFacade") IWalletService walletService,
                            @Qualifier("balanceFacade") ICoinService coinService){
        this.walletService = walletService;
        this.coinService = coinService;
    }

    // ---------- Coin Methods ----------

    @PostMapping("/coin")
    public ResponseEntity<Coin> addCoin(@RequestBody Coin coin){
        try{ return ResponseEntity.ok().body(coinService.addCoin(coin)); }
        catch (Exception e) {
            return new ResponseEntityBadRequest<Coin>().createBadRequest(e.getMessage());
        }
    }

    @GetMapping(path = "/coin/{id}")
    public ResponseEntity<Coin> getCoin(@PathVariable("id") String id){
        return ResponseEntity.ok().body(coinService.getCoin(id));
    }

    @DeleteMapping(path = "/coin/{id}")
    public ResponseEntity removeCoin(@PathVariable String id){
        try {
            coinService.removeCoin(id);
            return new ResponseEntity(HttpStatus.OK); }
        catch (DataAccessException dae){
            return new ResponseEntityBadRequest().createBadRequest("A coin that already is associated with other entities cannot be removed!");
        }
        catch (Exception e){
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
        }
    }

    @GetMapping("/coin")
    public ResponseEntity<List<Coin>> getListOfCoins(){
        return ResponseEntity.ok().body(coinService.getListOfCoins());
    }

    // ---------- Wallet Methods ----------

    @GetMapping(path = "/wallet/{id}")
    public ResponseEntity<Wallet> getWallet(@PathVariable("id") int id){
        return ResponseEntity.ok().body(walletService.getWallet(id));
    }

    @GetMapping("/gambler/{gambler_id}")
    public ResponseEntity<List<Wallet>> getGamblerWallets(@PathVariable("gambler_id") int gambler_id){
        return ResponseEntity.ok().body(walletService.getGamblerWallets(gambler_id));
    }

    @GetMapping("/wallet")
    public ResponseEntity<List<Wallet>> getListOfWallets(){
        return ResponseEntity.ok().body(walletService.getListOfWallets());
    }

    @PostMapping("/wallet")
    public ResponseEntity<Wallet> createWallet(@RequestBody Wallet wallet){
        try {
            return new ResponseEntity<>(walletService.createWallet(wallet),HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntityBadRequest<Wallet>().createBadRequest(e.getMessage());
        }
    }

    @DeleteMapping(path = "/wallet/{id}")
    public ResponseEntity removeWallet(@PathVariable int id){
        try {
            walletService.removeWallet(id);
            return new ResponseEntity(HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
        }
    }
}
package ras.adlrr.RASBet.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ras.adlrr.RASBet.api.auxiliar.ResponseEntityBadRequest;
import ras.adlrr.RASBet.model.Coin;
import ras.adlrr.RASBet.model.Wallet;
import ras.adlrr.RASBet.service.IWalletService;
import ras.adlrr.RASBet.service.WalletService;

@RequestMapping("/api/wallets")
@RestController
public class WalletController {
    private final IWalletService walletService;

    @Autowired
    public WalletController(IWalletService walletService){
        this.walletService = walletService;
    }

    // ---------- Coin Methods ----------

    @PostMapping("/coin")
    public ResponseEntity<Coin> addCoin(@RequestBody Coin coin){
        try{ return ResponseEntity.ok().body(walletService.addCoin(coin)); }
        catch (Exception e) {
            return new ResponseEntityBadRequest<Coin>().createBadRequest(e.getMessage());
        }
    }

    @GetMapping(path = "/coin/{id}")
    public ResponseEntity<Coin> getCoin(@PathVariable("id") String id){
        return ResponseEntity.ok().body(walletService.getCoin(id));
    }

    @DeleteMapping(path = "/coin/{id}")
    //TODO - So se consegue eliminar coins que n estejam relacionadas a outras entidades
    public ResponseEntity removeCoin(@PathVariable String id){
        try {
            walletService.removeCoin(id);
            return new ResponseEntity(HttpStatus.OK); }
        catch (Exception e){
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
        }
    }

    @GetMapping("/coin")
    public ResponseEntity<List<Coin>> getListOfCoins(){
        return ResponseEntity.ok().body(walletService.getListOfCoins());
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
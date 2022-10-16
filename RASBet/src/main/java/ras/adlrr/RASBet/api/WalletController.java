package ras.adlrr.RASBet.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ras.adlrr.RASBet.model.Coin;
import ras.adlrr.RASBet.model.Wallet;
import ras.adlrr.RASBet.service.WalletService;

@RequestMapping("/api/wallets/")
@RestController
public class WalletController {
    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService){
        this.walletService = walletService;
    }

    @PostMapping("/wallet")
    public int addWallet(@RequestBody Wallet wallet){
        return walletService.addWallet(wallet);
    }

    @GetMapping(path = "/wallet/{id}")
    public Wallet getWallet(@PathVariable("id") int id){
        return walletService.getWallet(id);
    }

    @DeleteMapping(path = "/wallet/{id}")
    public int removeWallet(@PathVariable int id){
        return walletService.removeWallet(id);
    }

    @GetMapping("/wallet")
    public List<Wallet> getListOfWallets(){
        return walletService.getListOfWallets();
    }

    @PostMapping("/coin")
    public int addCoin(@RequestBody Coin coin){
        return walletService.addCoin(coin);
    }

    @GetMapping(path = "/coin/{id}")
    public Coin getCoin(@PathVariable("id") int id){
        return walletService.getCoin(id);
    }

    @DeleteMapping(path = "/coin/{id}")
    public int removeCoin(@PathVariable int id){
        return walletService.removeCoin(id);
    }

    @GetMapping("/coin")
    public List<Coin> getListOfCoins(){
        return walletService.getListOfCoins();
    }
}
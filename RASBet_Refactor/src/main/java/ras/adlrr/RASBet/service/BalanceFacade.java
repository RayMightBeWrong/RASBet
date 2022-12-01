package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.model.Coin;
import ras.adlrr.RASBet.model.Gambler;
import ras.adlrr.RASBet.model.Wallet;
import ras.adlrr.RASBet.service.interfaces.balance.ICoinService;
import ras.adlrr.RASBet.service.interfaces.users.IGamblerService;
import ras.adlrr.RASBet.service.interfaces.balance.IWalletService;

import java.util.List;

@Service("balanceFacade")
public class BalanceFacade implements IWalletService, ICoinService {
    private final ICoinService coinService;
    private final IWalletService walletService;
    private final IGamblerService gamblerService;

    @Autowired
    public BalanceFacade(@Qualifier("coinService") ICoinService coinService, @Qualifier("walletService") IWalletService walletService,
                         @Qualifier("userFacade") IGamblerService gamblerService) {
        this.coinService = coinService;
        this.walletService = walletService;
        this.gamblerService = gamblerService;
    }

    // ---------- Coin Methods ----------

    public Coin getCoin(String id){
        return coinService.getCoin(id);
    }

    public Coin addCoin(Coin coin) throws Exception {
        return coinService.addCoin(coin);
    }

    public void removeCoin(String id) throws Exception {
        coinService.removeCoin(id);
    }

    public List<Coin> getListOfCoins(){
        return coinService.getListOfCoins();
    }

    public boolean coinExistsById(String id) {
        return coinService.coinExistsById(id);
    }

    // ---------- Wallet Methods ----------

    public Wallet getWallet(int id){
        return walletService.getWallet(id);
    }

    public List<Wallet> getGamblerWallets(int gambler_id){
        return walletService.getGamblerWallets(gambler_id);
    }

    public Wallet getWalletByGamblerIdAndCoinId(int gambler_id, String coin_id){
        return walletService.getWalletByGamblerIdAndCoinId(gambler_id, coin_id);
    }

    public List<Wallet> getListOfWallets(){
        return walletService.getListOfWallets();
    }

    public Wallet createWallet(Wallet wallet) throws Exception {
        Coin coin = wallet.getCoin();
        if(coin == null || !coinExistsById(coin.getId()))
            throw new Exception("Cannot create wallet for an invalid coin!");

        Gambler gambler = wallet.getGambler();
        if(gambler == null || !gamblerService.gamblerExistsById(gambler.getId()))
            throw new Exception("Cannot create wallet for an invalid gambler!");

        return walletService.createWallet(wallet);
    }

    public void removeWallet(int id) throws Exception {
        walletService.removeWallet(id);
    }

    public Wallet changeBalance(int wallet_id, float value) throws Exception{
        return walletService.changeBalance(wallet_id,value);
    }

    public boolean walletExistsById(int id) {
        return walletService.walletExistsById(id);
    }

    public Integer findGamblerIdByWalletId(int wallet_id){
        return walletService.findGamblerIdByWalletId(wallet_id);
    }

    public String getCoinIdFromWallet(int wallet_id){
        return walletService.getCoinIdFromWallet(wallet_id);
    }
}

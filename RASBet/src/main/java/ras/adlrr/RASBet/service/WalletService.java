package ras.adlrr.RASBet.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ras.adlrr.RASBet.dao.*;
import ras.adlrr.RASBet.model.Coin;
import ras.adlrr.RASBet.model.Gambler;
import ras.adlrr.RASBet.model.Promotions.IBalancePromotion;
import ras.adlrr.RASBet.model.Promotions.IPromotion;
import ras.adlrr.RASBet.model.Wallet;
import ras.adlrr.RASBet.service.PromotionServices.ClientPromotionService;
import ras.adlrr.RASBet.service.PromotionServices.PromotionService;
import javax.transaction.Transactional;
import ras.adlrr.RASBet.service.interfaces.ICoinService;
import ras.adlrr.RASBet.service.interfaces.IWalletService;

@Service
public class WalletService implements IWalletService, ICoinService{
    private final UserService userService;
    private final WalletRepository walletRepository;
    private final CoinRepository coinRepository;
    private final ClientPromotionService clientPromotionService;
    private final PromotionService promotionService;

    @Autowired
    public WalletService(WalletRepository walletRepository, CoinRepository coinRepository,
                         UserService userService,  ClientPromotionService clientPromotionService,
                         PromotionService promotionService){
        this.walletRepository = walletRepository;
        this.coinRepository = coinRepository;
        this.userService = userService;
        this.clientPromotionService = clientPromotionService;
        this.promotionService = promotionService;
    }

    // ---------- Coin Methods ----------

    /**
     * Checks for the existence of a coin with the given id. If the coin exists, returns it.
     * @param id Identification of the coin
     * @return coin if it exists, or null
     */
    public Coin getCoin(String id){
        return coinRepository.findById(id).orElse(null);
    }

    /**
     * Adds a coin to the repository
     * @param coin Coin to be persisted
     * @return coin updated by the repository
     * @throws Exception If any of the attributes does not meet the requirements an Exception is thrown indicating the error.
     */
    public Coin addCoin(Coin coin) throws Exception {
        if(coin.getRatio_EUR() <= 0)
            throw new Exception("The ratio cannot be 0 or negative.");
        return coinRepository.save(coin);
    }

    /**
     * If a coin with the given id exists, removes it from the repository
     * @param id Identification of the coin
     * @throws Exception If the coin does not exist.
     */
    public void removeCoin(String id) throws Exception {
        if(!coinRepository.existsById(id))
            throw new Exception("Coin does not exist!");
        coinRepository.deleteById(id);
    }

    /**
     * @return list of coins present in the repository
     */
    public List<Coin> getListOfCoins(){
        return coinRepository.findAll();
    }

    /**
     * Checks for the existence of a coin with the given id
     * @param id Identification of the coin
     * @return true if a coin exists with the given identification
     */
    public boolean coinExistsById(String id) {
        return coinRepository.existsById(id);
    }

    // ---------- Wallet Methods ----------

    /**
     * Checks for the existence of a wallet with the given id. If the wallet exists, returns it.
     * @param id Identification of the wallet
     * @return wallet if it exists, or null
     */
    public Wallet getWallet(int id){
        return walletRepository.findById(id).orElse(null);
    }

    /**
     * @param gambler_id Identification of the gambler that owns the wallets
     * @return list of wallets of a gambler present in the repository
     */
    public List<Wallet> getGamblerWallets(int gambler_id){
        return walletRepository.findAllByGamblerId(gambler_id);
    }

    /**
     * @return list of wallets present in the repository
     */
    public List<Wallet> getListOfWallets(){
        return walletRepository.findAll();
    }

    /**
     * Creates a wallet in the repository
     * @param wallet Wallet to be persisted
     * @return wallet updated by the repository
     * @throws Exception If any of the attributes does not meet the requirements an Exception is thrown indicating the error.
     */
    public Wallet createWallet(Wallet wallet) throws Exception {
        wallet.setId(0); //Grants that a wallet is created, instead of updating an existing wallet
        wallet.setBalance(0); //Grants that a wallet is created with balance equal to 0
        wallet.setTransactions(null); //Grants that a wallet does not start with transactions associated

        Coin coin = wallet.getCoin();
        if(coin == null || !coinExistsById(coin.getId()))
            throw new Exception("Cannot create wallet for an invalid coin!");

        Gambler gambler = wallet.getGambler();
        if(gambler == null || !userService.gamblerExistsById(gambler.getId()))
            throw new Exception("Cannot create wallet for an invalid gambler!");

        return walletRepository.save(wallet);
    }

    /**
     * If a wallet with the given id exists, removes it from the repository
     * @param id Identification of the wallet
     * @throws Exception If the wallet does not exist.
     */
    public void removeWallet(int id) throws Exception {
        if(!walletExistsById(id))
            throw new Exception("Wallet needs to exist to be removed!");
        walletRepository.deleteById(id);
    }

    /**
     * Adds a value to a wallet's balance.
     * @param wallet_id Identification of the wallet.
     * @param valueToAdd Value to add to the balance.
     * @return updated wallet
     * @throws Exception If the value is negative.
     */
    public Wallet addToBalance(int wallet_id, float valueToAdd) throws Exception {
        if (valueToAdd < 0) throw new Exception("Cannot perform the operation with a negative value!");
        return changeBalance(wallet_id, valueToAdd);
    }

    /**
     * Removes a value from a wallet's balance.
     * @param wallet_id Identification of the wallet.
     * @param valueToRemove Value to remove from the balance.
     * @return updated wallet
     * @throws Exception If the value is negative.
     */
    public Wallet removeFromBalance(int wallet_id, float valueToRemove) throws Exception {
        if (valueToRemove < 0) throw new Exception("Cannot perform the operation with a negative value!");
        return changeBalance(wallet_id, -valueToRemove);
    }

    /**
     * Adds/Removes a value from the balance of a wallet.
     * @param wallet_id Identification of the wallet.
     * @param value Value to add/remove to the balance. If the value is positive then the value is added, if negative then it is removed.
     * @return updated wallet
     * @throws Exception If the wallet with the given id does not exist, or if there are no sufficient funds to remove from the wallet.
     */
    private Wallet changeBalance(int wallet_id, float value) throws Exception {
        Wallet wallet = walletRepository.findById(wallet_id).orElse(null);
        if(wallet == null)
            throw new Exception("Cannot perform deposit operation to a non existent wallet!");
        if(!wallet.changeBalance(value))
            throw new Exception("Wallet does not have sufficient funds to execute the withdraw operation!");
        return walletRepository.save(wallet);
    }

    /**
     * Claims the balance offered by a balance promotion
     * @param wallet_id Identification of the wallet that is supposed to receive the balance
     * @param coupon Identification of the balance promotion
     * @throws Exception If a necessary condition is not met. The message contains the error.
     */
    @Transactional(rollbackOn = {Exception.class}, value = Transactional.TxType.REQUIRES_NEW)
    public void claimBalancePromotion(int wallet_id, String coupon) throws Exception {
        Wallet wallet = getWallet(wallet_id);
        if(wallet == null)
            throw new Exception("Cannot claim the balance to a invalid wallet.");

        //Checks if coupon belongs to a balance promotion
        IPromotion promotion = promotionService.getPromotionByCoupon(coupon);
        if(!(promotion instanceof IBalancePromotion balancePromotion))
            throw new Exception("Invalid coupon.");

        //Checks if the coin of the wallet matches the coin of the promotion
        if(!balancePromotion.getCoin().getId().equals(wallet.getCoin().getId()))
            throw new Exception("Coin of the wallet does not match the coin of the promotion.");

        clientPromotionService.claimPromotionWithCoupon(wallet.getGambler().getId(), coupon);

        addToBalance(wallet_id, balancePromotion.getValue_to_give());
    }

    /**
     * Checks for the existence of a wallet with the given id
     * @param id Identification of the wallet
     * @return true if a wallet exists with the given identification
     */
    public boolean walletExistsById(int id) {
        return walletRepository.existsById(id);
    }

    /**
     * Using the identification of the wallet, finds the identification of its owner.
     * @param wallet_id Identification of the wallet
     * @return identification of the gambler that owns the wallet.
     */
    public Integer findGamblerIdByWalletId(int wallet_id){
        return walletRepository.findGamblerIdByWalletId(wallet_id).orElse(null);
    }

    public String getCoinIdFromWallet(int wallet_id){
        return walletRepository.getCoinIdFromWallet(wallet_id);
    }
}
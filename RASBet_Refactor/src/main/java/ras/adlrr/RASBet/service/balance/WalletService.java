package ras.adlrr.RASBet.service.balance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.WalletRepository;
import ras.adlrr.RASBet.model.Coin;
import ras.adlrr.RASBet.model.Gambler;
import ras.adlrr.RASBet.model.Wallet;
import ras.adlrr.RASBet.service.interfaces.balance.IWalletService;

import java.util.List;

@Service("walletService")
public class WalletService implements IWalletService {
    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

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

    public Wallet getWalletByGamblerIdAndCoinId(int gambler_id, String coin_id){
        return walletRepository.getByGamblerIdAndCoinId(gambler_id, coin_id);
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
        if(coin == null)
            throw new Exception("Cannot create wallet without coin!");

        Gambler gambler = wallet.getGambler();
        if(gambler == null)
            throw new Exception("Cannot create wallet without a gambler!");

        if(hasWalletWithCoin(gambler.getId(), coin.getId()))
            throw new Exception("Gambler already has a wallet that uses this coin.");

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
     * Adds/Removes a value from the balance of a wallet.
     * @param wallet_id Identification of the wallet.
     * @param value Value to add/remove to the balance. If the value is positive then the value is added, if negative then it is removed.
     * @return updated wallet
     * @throws Exception If the wallet with the given id does not exist, or if there are no sufficient funds to remove from the wallet.
     */
    public Wallet changeBalance(int wallet_id, float value) throws Exception {
        Wallet wallet = walletRepository.findById(wallet_id).orElse(null);
        if(wallet == null)
            throw new Exception("Cannot perform deposit operation to a non existent wallet!");
        if(!wallet.changeBalance(value))
            throw new Exception("Wallet does not have sufficient funds to execute the withdraw operation!");
        return walletRepository.save(wallet);
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

    public boolean hasWalletWithCoin(int gambler_id, String coin_id){
        return walletRepository.hasWalletWithCoin(gambler_id, coin_id);
    }
}

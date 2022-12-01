package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.model.*;
import ras.adlrr.RASBet.model.Promotions.interfaces.IBalancePromotion;
import ras.adlrr.RASBet.model.Promotions.interfaces.IPromotion;
import ras.adlrr.RASBet.service.interfaces.*;
import ras.adlrr.RASBet.service.interfaces.balance.ICoinService;
import ras.adlrr.RASBet.service.interfaces.balance.IWalletService;
import ras.adlrr.RASBet.service.interfaces.notifications.INotificationService;
import ras.adlrr.RASBet.service.interfaces.promotions.IClientPromotionService;
import ras.adlrr.RASBet.service.interfaces.promotions.IPromotionService;
import ras.adlrr.RASBet.service.interfaces.transactions.ITransactionService;
import ras.adlrr.RASBet.service.interfaces.users.IGamblerService;

import javax.transaction.Transactional;
import java.util.List;

@Service("transactionFacade")
public class TransactionFacade implements ITransactionService, IUserTransactions {
    private final ITransactionService transactionService;
    private final IGamblerService gamblerService;
    private final IWalletService walletService;
    private final ICoinService coinService;
    private final IPromotionService promotionService;
    private final IClientPromotionService clientPromotionService;
    private final INotificationService notificationService;

    @Autowired
    public TransactionFacade (@Qualifier("transactionService") ITransactionService transactionService,
                              @Qualifier("userFacade") IGamblerService gamblerService,
                              @Qualifier("balanceFacade") IWalletService walletService,
                              @Qualifier("balanceFacade") ICoinService coinService,
                              @Qualifier("promotionsFacade") IPromotionService promotionService,
                              @Qualifier("promotionsFacade") IClientPromotionService clientPromotionService,
                              INotificationService notificationService){
        this.transactionService = transactionService;
        this.gamblerService = gamblerService;
        this.walletService = walletService;
        this.coinService = coinService;
        this.notificationService = notificationService;
        this.clientPromotionService = clientPromotionService;
        this.promotionService = promotionService;
    }


    /* ******* Transaction Methods ******* */
    /**
     * Checks for the existence of a transaction with the given id. If the transaction exists, returns it.
     * @param id Identification of the transaction.
     * @return transaction if it exists, or null.
     */
    public Transaction getTransaction(int id) {
        return transactionService.getTransaction(id);
    }

    /**
     * @param gambler_id Identification of the gambler that made the transactions.
     * @param direction Defines the order of the transactions, by date. If 'null', no order is imposed.
     * @return list of transactions of a gambler present in the repository.
     */
    public List<Transaction> getGamblerTransactions(int gambler_id, Sort.Direction direction) {
        return transactionService.getGamblerTransactions(gambler_id, direction);
    }

    /**
     * @param gambler_id Identification of the gambler that made the transactions.
     * @return list of transactions of a gambler present in the repository.
     */
    public List<Transaction> getGamblerTransactions(int gambler_id){
        return getGamblerTransactions(gambler_id, null);
    }

    /**
     * @return list of transactions present in the repository.
     */
    public List<Transaction> getTransactions() {
        return transactionService.getTransactions();
    }

    /**
     * Adds a transaction to the repository.
     * @param t Transaction to be persisted.
     * @return transaction updated by the repository.
     * @throws Exception If any of the attributes does not meet the requirements an Exception is thrown indicating the error.
     */
    public Transaction addTransaction(Transaction t) throws Exception {
        if(t == null) throw new Exception("Null Transaction!");

        //Transaction must have a valid gambler associated
        Gambler gambler = t.getGambler();
        if(gambler == null || !gamblerService.gamblerExistsById(gambler.getId()))
            throw new Exception("Cannot register a transaction to a non existent gambler!");

        //Transaction must have a valid coin associated
        Coin coin = t.getCoin();
        if(coin == null || !coinService.coinExistsById(coin.getId()))
            throw new Exception("Cannot register a transaction with a non existent coin!");

        //Transaction if related to a wallet, then the wallet must be valid
        Wallet wallet = t.getWallet();
        if(wallet != null){
            if(!walletService.walletExistsById(wallet.getId()))
                throw new Exception("Invalid wallet!");
            else {
                wallet = walletService.changeBalance(wallet.getId(), t.getValue());
                t.setWallet(wallet);
            }
        }

        return transactionService.addTransaction(t);
    }

    /**
     * If a transaction with the given identification exists, removes it from the repository.
     * @param id identification of the transaction.
     * @throws Exception If the transaction does not exist.
     */
    public void removeTransaction(int id) throws Exception {
        transactionService.removeTransaction(id);
    }


    /* ******* User Specific Transactions Methods ******* */

    /**
     * Perform a deposit transaction.
     * @param wallet_id identification of the wallet that is the aim of the deposit.
     * @param valueToDeposit value to deposit.
     * @return transaction persisted in the repository.
     * @throws Exception if the value is negative.
     */
    @Transactional(rollbackOn = {Exception.class, DataAccessException.class}, value = Transactional.TxType.REQUIRES_NEW)
    public Transaction deposit(int wallet_id, float valueToDeposit) throws Exception {
        Wallet wallet = walletService.getWallet(wallet_id);
        if(wallet == null)
            throw new Exception("Cannot deposit a value into a non existent wallet!");
        if(valueToDeposit < 0)
            throw new Exception("Value to deposit cannot be negative.");


        Transaction transaction = new Transaction();
        Gambler gambler = wallet.getGambler();

        transaction.setCoin(wallet.getCoin());
        transaction.setWallet(wallet);
        transaction.setValue(valueToDeposit);
        transaction.setDescription("Deposit");
        transaction.setGambler(gambler);

        Transaction res = addTransaction(transaction);

        String email = gamblerService.getGamblerEmail(gambler.getId());
        String message = "A deposit has been made in your RASBet account.";
        String subject = "[RASBet] Deposit Made";
        Notification notification = new Notification(gambler.getId(), email, message, subject);
        notificationService.addNotification(notification);

        return res;
    }

    /**
     * Perform a withdrawal transaction.
     * @param wallet_id identification of the wallet that is the aim of the withdrawal.
     * @param valueToWithdraw value to withdraw.
     * @return transaction persisted in the repository.
     * @throws Exception if the value is negative.
     */
    @Transactional(rollbackOn = {Exception.class, DataAccessException.class}, value = Transactional.TxType.REQUIRES_NEW)
    public Transaction withdraw(int wallet_id, float valueToWithdraw) throws Exception {
        Wallet wallet = walletService.getWallet(wallet_id);
        if(wallet == null)
            throw new Exception("Cannot withdraw a value from a non existent wallet!");
        if(valueToWithdraw < 0)
            throw new Exception("Value to withdraw cannot be negative.");

        Transaction transaction = new Transaction();
        Gambler gambler = wallet.getGambler();

        transaction.setCoin(wallet.getCoin());
        transaction.setWallet(wallet);
        transaction.setValue(-valueToWithdraw);
        transaction.setDescription("Withdraw");
        transaction.setGambler(wallet.getGambler());

        Transaction res = addTransaction(transaction);

        String email = gamblerService.getGamblerEmail(gambler.getId());
        String message = "A withdrawal has been made in your RASBet account.";
        String subject = "[RASBet] Withdrawal Made";
        Notification notification = new Notification(gambler.getId(), email, message, subject);
        notificationService.addNotification(notification);

        return res;
    }

    /**
     * Claims the balance offered by a balance promotion
     * @param wallet_id Identification of the wallet that is supposed to receive the balance
     * @param coupon Identification of the balance promotion
     * @throws Exception If a necessary condition is not met. The message contains the error.
     */
    @Transactional(rollbackOn = {Exception.class}, value = Transactional.TxType.REQUIRES_NEW)
    public Transaction claimBalancePromotion(int wallet_id, String coupon) throws Exception {
        Wallet wallet = walletService.getWallet(wallet_id);
        if(wallet == null)
            throw new Exception("Cannot claim the balance to a invalid wallet.");
        int gambler_id = wallet.getGambler().getId();

        //Checks if coupon belongs to a balance promotion
        IPromotion promotion = promotionService.getPromotionByCoupon(coupon);
        if(!(promotion instanceof IBalancePromotion balancePromotion))
            throw new Exception("Invalid coupon.");
        String promo_coin_id = balancePromotion.getCoin().getId();
        float balanceToGive = balancePromotion.getValue_to_give();

        //Checks if the coin of the wallet matches the coin of the promotion
        if(!promo_coin_id.equals(wallet.getCoin().getId()))
            throw new Exception("Coin of the wallet does not match the coin of the promotion.");

        clientPromotionService.claimPromotionWithCoupon(gambler_id, coupon);

        Transaction transaction = new Transaction(gambler_id, wallet_id, wallet.getBalance(),
                "Claimed balance with promotion coupon " + coupon + ".",
                balanceToGive, promo_coin_id);

        return addTransaction(transaction);
    }
}

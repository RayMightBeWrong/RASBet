package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.*;
import ras.adlrr.RASBet.model.*;
import ras.adlrr.RASBet.model.Promotions.interfaces.IBalancePromotion;
import ras.adlrr.RASBet.model.Promotions.interfaces.IPromotion;
import ras.adlrr.RASBet.service.PromotionServices.ClientPromotionService;
import ras.adlrr.RASBet.service.PromotionServices.PromotionService;
import ras.adlrr.RASBet.service.interfaces.INotificationService;
import ras.adlrr.RASBet.service.interfaces.ITransactionService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Service
public class TransactionService implements ITransactionService{
    private final TransactionRepository transactionRepository;
    private final UserService userService;
    private final WalletService walletService;
    private final INotificationService notificationService;
    private final ClientPromotionService clientPromotionService;
    private final PromotionService promotionService;

    @Autowired
    public TransactionService (TransactionRepository transactionRepository, UserService userService, 
                               WalletService walletService, INotificationService notificationService,
                               ClientPromotionService clientPromotionService, PromotionService promotionService){
        this.transactionRepository = transactionRepository;
        this.userService = userService;
        this.walletService = walletService;
        this.notificationService = notificationService;
        this.clientPromotionService = clientPromotionService;
        this.promotionService = promotionService;
    }

    /**
     * Checks for the existence of a transaction with the given id. If the transaction exists, returns it.
     * @param id Identification of the transaction.
     * @return transaction if it exists, or null.
     */
    public Transaction getTransaction(int id) {
        return transactionRepository.findById(id).orElse(null);
    }

    /**
     * @param gambler_id Identification of the gambler that made the transactions.
     * @param direction Defines the order of the transactions, by date. If 'null', no order is imposed.
     * @return list of transactions of a gambler present in the repository.
     * @throws Exception If the gambler does not exist.
     */
    public List<Transaction> getGamblerTransactions(int gambler_id, Sort.Direction direction) throws Exception {
        if(!userService.gamblerExistsById(gambler_id))
            throw new Exception("Gambler does not exist!");
        return direction == null ? transactionRepository.findAllByGamblerId(gambler_id) :
                transactionRepository.findAllByGamblerId(gambler_id, Sort.by(direction, "date"));
    }

    /**
     * @param gambler_id Identification of the gambler that made the transactions.
     * @return list of transactions of a gambler present in the repository.
     * @throws Exception If the gambler does not exist.
     */
    public List<Transaction> getGamblerTransactions(int gambler_id) throws Exception {
        return getGamblerTransactions(gambler_id, null);
    }

    /**
     * @return list of transactions present in the repository.
     */
    public List<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    /**
     * Adds a transaction to the repository.
     * @param t Transaction to be persisted.
     * @return transaction updated by the repository.
     * @throws Exception If any of the attributes does not meet the requirements an Exception is thrown indicating the error.
     */
    public Transaction addTransaction(Transaction t) throws Exception {
        //Cannot persist a null transaction
        if(t == null)
            throw new Exception("Null Transaction!");

        //Transaction must have a valid gambler associated
        Gambler gambler = t.getGambler();
        if(gambler == null || !userService.gamblerExistsById(gambler.getId()))
            throw new Exception("Cannot register a transaction to a non existent gambler!");

        //Transaction must have a valid coin associated
        Coin coin = t.getCoin();
        if(coin == null || !walletService.coinExistsById(coin.getId()))
            throw new Exception("Cannot register a transaction with a non existent coin!");

        //Transaction must have a valid value associated
        if (t.getValue() < 0)
            throw new Exception("Cannot perform a transaction with a negative value!");

        //Transaction if related to a wallet, then the wallet must be valid, and the balance of the wallet after the transaction must not be negative
        Wallet wallet = t.getWallet();
        if(wallet != null) {
            if(!walletService.walletExistsById(wallet.getId()))
                throw new Exception("Invalid wallet!");

            Float balance_after_mov = t.getBalance_after_mov();
            if(balance_after_mov != null && balance_after_mov < 0)
                throw new Exception("Balance cannot be negative!");
        }

        //Sets the date of the transaction to the current date
        t.setDate(LocalDateTime.now(ZoneId.of("UTC+00:00")));

        return transactionRepository.save(t);
    }

    /**
     * Perform a deposit transaction.
     * @param wallet_id identification of the wallet that is the aim of the deposit.
     * @param valueToDeposit value to deposit.
     * @return transaction persisted in the repository.
     * @throws Exception if the value is negative.
     */
    public Transaction deposit(int wallet_id, float valueToDeposit) throws Exception {
        Wallet wallet = walletService.addToBalance(wallet_id, valueToDeposit);

        Transaction transaction = new Transaction();
        Coin coin = wallet.getCoin();
        Gambler gambler = wallet.getGambler();

        transaction.setCoin(coin);
        transaction.setWallet(wallet);
        transaction.setValue(valueToDeposit);
        transaction.setDescription("Deposit");
        transaction.setBalance_after_mov(wallet.getBalance());
        transaction.setGambler(gambler);

        Transaction res = addTransaction(transaction);

        String email = userService.getGamblerEmail(gambler.getId());
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
    public Transaction withdraw(int wallet_id, float valueToWithdraw) throws Exception {
        Wallet wallet = walletService.removeFromBalance(wallet_id, valueToWithdraw);

        Transaction transaction = new Transaction();
        Gambler gambler = wallet.getGambler();

        transaction.setCoin(wallet.getCoin());
        transaction.setWallet(wallet);
        transaction.setValue(valueToWithdraw);
        transaction.setDescription("Withdraw");
        transaction.setBalance_after_mov(wallet.getBalance());
        transaction.setGambler(wallet.getGambler());

        Transaction res = addTransaction(transaction);

        String email = userService.getGamblerEmail(gambler.getId());
        String message = "A withdrawal has been made in your RASBet account.";
        String subject = "[RASBet] Withdrawal Made";
        Notification notification = new Notification(gambler.getId(), email, message, subject);
        notificationService.addNotification(notification);

        return res;
    }

    /**
     * If a transaction with the given identification exists, removes it from the repository.
     * @param id identification of the transaction.
     * @throws Exception If the transaction does not exist.
     */
    public void removeTransaction(int id) throws Exception {
        if(!transactionRepository.existsById(id))
            throw new Exception("Transaction needs to exist, to be removed!");
        transactionRepository.deleteById(id);
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
        String coin_id = balancePromotion.getCoin().getId();
        float balanceToGive = balancePromotion.getValue_to_give();

        //Checks if the coin of the wallet matches the coin of the promotion
        if(!coin_id.equals(wallet.getCoin().getId()))
            throw new Exception("Coin of the wallet does not match the coin of the promotion.");

        clientPromotionService.claimPromotionWithCoupon(gambler_id, coupon);

        wallet = walletService.addToBalance(wallet_id, balancePromotion.getValue_to_give());

        Transaction transaction = new Transaction(gambler_id, wallet_id, wallet.getBalance(),
                                                  "Claimed balance with promotion coupon " + coupon + ".",
                                                  balanceToGive, coin_id, LocalDateTime.now(ZoneId.of("UTC+00:00")));

        return transactionRepository.save(transaction);
    }
}
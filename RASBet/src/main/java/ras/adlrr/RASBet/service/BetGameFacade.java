package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import ras.adlrr.RASBet.model.*;
import ras.adlrr.RASBet.model.Promotions.interfaces.IBoostOddPromotion;
import ras.adlrr.RASBet.service.interfaces.IBetGameService;
import ras.adlrr.RASBet.service.interfaces.balance.IWalletService;
import ras.adlrr.RASBet.service.interfaces.bets.IBetService;
import ras.adlrr.RASBet.service.interfaces.notifications.INotificationService;
import ras.adlrr.RASBet.service.interfaces.promotions.IClientPromotionService;
import ras.adlrr.RASBet.service.interfaces.promotions.IPromotionService;
import ras.adlrr.RASBet.service.interfaces.sports.IGameService;
import ras.adlrr.RASBet.service.interfaces.transactions.ITransactionService;
import ras.adlrr.RASBet.service.interfaces.users.IGamblerService;


@Service("betGameFacade")
public class BetGameFacade implements IBetService, IBetGameService {
    private final IBetService betService;
    private final ITransactionService transactionService;
    private final IWalletService walletService;
    private final IGamblerService gamblerService;
    private final IGameService gameService;
    private final IClientPromotionService clientPromotionService;
    private final IPromotionService promotionService;
    private final INotificationService notificationService;


    @Autowired
    public BetGameFacade(@Qualifier("betService") IBetService betService,
                         @Qualifier("transactionFacade") ITransactionService transactionService,
                         @Qualifier("balanceFacade") IWalletService walletService,
                         @Qualifier("userFacade") IGamblerService gamblerService,
                         @Qualifier("sportsFacade") IGameService gameService,
                         @Qualifier("promotionsFacade") IClientPromotionService clientPromotionService,
                         @Qualifier("promotionsFacade") IPromotionService promotionService,
                         INotificationService notificationService) {
        this.betService = betService;
        this.transactionService = transactionService;
        this.walletService = walletService;
        this.gamblerService = gamblerService;
        this.gameService = gameService;
        this.clientPromotionService = clientPromotionService;
        this.promotionService = promotionService;
        this.notificationService = notificationService;
    }

    /* ********* IBetService Methods ********* */

    /**
     * Checks for the existence of a bet with the given id. If the bet exists, returns it.
     * @param id Identification of the bet
     * @return bet if it exists, or null
     */
    public Bet getBet(int id) {
        return betService.getBet(id);
    }

    /**
     * Adds a bet to the repository
     * @param bet Bet to be persisted
     * @return bet updated by the repository
     * @throws Exception If any of the attributes does not meet the requirements an Exception is thrown indicating the error.
     */
    @Transactional(rollbackOn = {Exception.class}, value = Transactional.TxType.REQUIRES_NEW)
    public Bet addBet(Bet bet) throws Exception {
        //Cannot add a null bet to the repository
        if(bet == null)
            throw new Exception("Null Bet!");

        //A bet needs to have transaction data associated, and the value of the bet must be positive
        Transaction transaction = bet.getTransaction();
        if(transaction == null)
            throw new Exception("Null Transaction!");

        if(transaction.getValue() <= 0)
            throw new Exception("Value of the bet must be positive.");

        //Sets the value of the transaction to its symmetric,
        // because the method "addTransaction" needs the value to be negative,
        // in order to deduce a value from the wallets balance
        transaction.setValue(-transaction.getValue());


        //Check if the gambler has not already bet in the game
        Gambler gambler = transaction.getGambler();
        if(gambler == null)
            throw new Exception("Bet needs to have a gambler associated.");
        int gambler_id = gambler.getId();

        //Checks if the transaction is performed using a wallet, and if the coin used by the wallet matches the one from the transaction,
        //In case the transaction is made using a wallet, performs the billing operation and updates the transaction information
        Wallet wallet = transaction.getWallet();
        if(wallet != null) {
            Coin transactionCoin = transaction.getCoin();
            if(transactionCoin == null || !walletService.getCoinIdFromWallet(wallet.getId()).equals(transactionCoin.getId()))
                throw new Exception("Coin of the transaction does not match the coin from the wallet!");
        }
        transaction.setDescription("Bet expenses");

        //Claims the promotion
        String coupon = bet.getCoupon();
        if (coupon != null)
            clientPromotionService.claimPromotionWithCoupon(gambler_id, bet.getCoupon());

        //Persists the transaction
        transaction = transactionService.addTransaction(transaction);

        //Updates the bet and persists it
        bet.setTransaction(transaction);
        bet.setId(transaction.getId());
        bet = betService.addBet(bet);

        //Sends notification
        String email = gamblerService.getGamblerEmail(gambler.getId());
        String message = "A bet has been made in your RASBet account.";
        String subject = "[RASBet] Bet Made";
        Notification notification = new Notification(gambler.getId(), email, message, subject);
        notificationService.addNotification(notification);

        return bet;
    }

    /**
     * If a bet with the given id exists, removes it from the repository
     * @param betID Identification of the bet
     * @throws Exception If the bet does not exist.
     */
    public void removeBet(int betID) throws Exception {
        betService.removeBet(betID);
    }

    /**
     * @param gambler_id Identification of the gambler that made the transactions.
     * @param direction Defines the order of the bets, by date. If 'null', no order is imposed.
     * @return list of transactions of a gambler present in the repository.
     * @throws Exception If the gambler does not exist.
     */
    public List<Bet> getGamblerBets(int gambler_id, Sort.Direction direction) throws Exception {
        if(!gamblerService.gamblerExistsById(gambler_id))
            throw new Exception("Gambler does not exist!");
        return betService.getGamblerBets(gambler_id, direction);
    }

    /**
     * @param gambler_id Identification of the gambler that made the bets
     * @return list of bets of a gambler present in the repository
     * @throws Exception If the gambler does not exist.
     */
    public List<Bet> getGamblerBets(int gambler_id) throws Exception {
        if(!gamblerService.gamblerExistsById(gambler_id))
            throw new Exception("Gambler does not exist!");
        return betService.getGamblerBets(gambler_id);
    }

    /**
     * Withdraws the winnings of a bet to a wallet, if all the games have ended and all the game choices are correct
     * @param bet_id Identification of the bet
     * @return transaction of the winnings' withdrawal, or null if at least one game choice is not correct
     * @throws Exception If any error occurs during the withdrawal
     */
    @Transactional(rollbackOn = {Exception.class, DataAccessException.class}, value = Transactional.TxType.REQUIRES_NEW)
    public float closeBet(int bet_id) throws Exception{
        //Close bet and calculate the winnings
        float winnings = betService.closeBet(bet_id);

        Bet bet = getBet(bet_id);
        Transaction bet_transaction = bet.getTransaction();
        int gambler_id = bet_transaction.getGambler().getId();
        String coin_id = bet_transaction.getCoin().getId();

        //Gets the gambler's wallet that has the same coin has the one used to bet
        Wallet wallet_withdraw = walletService.getWalletByGamblerIdAndCoinId(gambler_id, coin_id);

        //If the gambler does not have a wallet with the specific coin, then a wallet is created
        if(wallet_withdraw == null)
            wallet_withdraw = walletService.createWallet(new Wallet(coin_id, gambler_id));

        //If the winnings equal to 0, then the bet was lost
        if(winnings == 0) {
            //Notifies the gambler of the lost bet
            Gambler gambler = wallet_withdraw.getGambler();
            String email = gamblerService.getGamblerEmail(gambler.getId());
            String message = "Unfortunately, it seems that you have lost a bet.";
            String subject = "[RASBet] Bet Lost";
            Notification notification = new Notification(gambler.getId(), email, message, subject);
            notificationService.addNotification(notification);
            return 0;
        }else {
            //Updates winnings if a boost odd promotion is active
            String coupon = bet.getCoupon();

            if(coupon != null) {
                //Applying the boost odd promotion
                var promotion = promotionService.getPromotionByCoupon(coupon);
                if (!(promotion instanceof IBoostOddPromotion boostOddPromotion))
                    throw new Exception("Invalid coupon given!");
                winnings *= 1 + (boostOddPromotion.getBoostOddPercentage() / 100);
            }
        }

        //Performs the transaction
        var wallet_id = wallet_withdraw.getId();
        Transaction newTransaction = new Transaction(gambler_id, wallet_id, wallet_withdraw.getBalance(),
                                         "Bet Winnings", winnings, coin_id);
        newTransaction = transactionService.addTransaction(newTransaction);

        //Notifies the gambler that he won the bet
        String email = gamblerService.getGamblerEmail(gambler_id);
        String message = "Congratulations! You just won a bet!";
        String subject = "[RASBet] Bet Won";
        Notification notification = new Notification(gambler_id, email, message, subject);
        notificationService.addNotification(notification);

        return winnings;
    }

    public void updateGames() throws Exception{
        String error = "";

        try {
            List<Game> toClose = gameService.updateGames();
            for(Game g: toClose)
                closeGameAndWithdrawBets(g.getId());
        }
        catch (Exception e){error += e.getMessage(); }

        if(!error.equals(""))
            throw new Exception(error);
    }

    @Transactional
    @Override
    public List<Bet> getBetsByGameId(int game_id) throws Exception {
        return betService.getBetsByGameId(game_id);
    }

    @Override
    public List<Integer> getBetsIdsByGameId(int game_id) throws Exception{
        return betService.getBetsIdsByGameId(game_id);
    }

    /* ********* IBetGameService Methods ********* */

    @Override
    public void closeGameAndWithdrawBets(int game_id) throws Exception {
        //Closes game
        gameService.closeGame(game_id);

        //Gets game's list of bets
        var bets = getBetsIdsByGameId(game_id);

        //Withdraws the bets that can be withdrawn
        for(Integer bet_id : bets){
            try {
                closeBet(bet_id);
            }catch (Exception ignored){ignored.printStackTrace();}
        }
    }
}
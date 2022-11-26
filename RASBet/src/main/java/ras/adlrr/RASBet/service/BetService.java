package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.model.Promotions.interfaces.IBoostOddPromotion;
import ras.adlrr.RASBet.service.PromotionServices.ClientPromotionService;
import ras.adlrr.RASBet.service.PromotionServices.PromotionService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import ras.adlrr.RASBet.dao.BetRepository;
import ras.adlrr.RASBet.model.*;
import ras.adlrr.RASBet.service.interfaces.IBetService;
import ras.adlrr.RASBet.service.interfaces.IGamblerService;
import ras.adlrr.RASBet.service.interfaces.INotificationService;

@Service
public class BetService implements IBetService{
    private final TransactionService transactionService;
    private final WalletService walletService;
    private final IGamblerService gamblerService;
    private final GameService gameService;
    private final ClientPromotionService clientPromotionService;
    private final BetRepository betRepository;
    private final PromotionService promotionService;
    private final INotificationService notificationService;

    @Autowired
    public BetService(TransactionService transactionService, WalletService walletService,
                      BetRepository betRepository, IGamblerService gamblerService,
                      GameService gameService, ClientPromotionService clientPromotionService,
                      PromotionService promotionService, INotificationService notificationService) {
        this.transactionService = transactionService;
        this.walletService = walletService;
        this.betRepository = betRepository;
        this.gamblerService = gamblerService;
        this.gameService = gameService;
        this.clientPromotionService = clientPromotionService;
        this.promotionService = promotionService;
        this.notificationService = notificationService;
    }

    /**
     * Checks for the existence of a bet with the given id. If the bet exists, returns it.
     * @param id Identification of the bet
     * @return bet if it exists, or null
     */
    public Bet getBet(int id) {
        return betRepository.findById(id).orElse(null);
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
            throw new Exception("Bet's value must be positive.");

        //Check if the gambler has not already bet in the game
        Gambler gambler = transaction.getGambler();
        if(gambler == null)
            throw new Exception("Bet needs to have a gambler associated.");
        int gambler_id = gambler.getId();

        //Validates the game choices
        List<GameChoice> gameChoices = bet.getGameChoices();
        if(gameChoices != null)
            gameChoices = gameChoices.stream().filter(Objects::nonNull).toList();
        validateGameChoices(gambler_id, gameChoices);

        //Sets the odd of the game choice to the current odd of the game participant
        gameService.giveOddToGameChoices(gameChoices);

        //Checks if the transaction is performed using a wallet, and if the coin used by the wallet matches the one from the transaction,
        //In case the transaction is made using a wallet, performs the billing operation and updates the transaction information
        Wallet wallet = transaction.getWallet();
        if(wallet != null) {
            Coin transactionCoin = transaction.getCoin();
            if(!walletService.getCoinIdFromWallet(wallet.getId()).equals(transactionCoin.getId()))
                throw new Exception("Coin of the transaction does not match the coin from the wallet!");

            wallet = walletService.removeFromBalance(wallet.getId(), transaction.getValue());
            transaction.setWallet(wallet);
            transaction.setBalance_after_mov(wallet.getBalance());
        }
        transaction.setDescription("Bet expenses");

        //Claims the promotion
        clientPromotionService.claimPromotionWithCoupon(gambler_id, bet.getCoupon());

        //Persists the transaction
        transaction = transactionService.addTransaction(transaction);

        //Updates the bet and persists it
        bet.setTransaction(transaction);
        bet.setId(transaction.getId());
        Bet res = betRepository.save(bet);

        //Sends notification
        String email = gamblerService.getGamblerEmail(gambler.getId());
        String message = "A bet has been made in your RASBet account.";
        String subject = "[RASBet] Bet Made";
        Notification notification = new Notification(gambler.getId(), email, message, subject);
        notificationService.addNotification(notification);

        return res;
    }

    /**
     * If a bet with the given id exists, removes it from the repository
     * @param betID Identification of the bet
     * @throws Exception If the bet does not exist.
     */
    public void removeBet(int betID) throws Exception {
        if(!betRepository.existsById(betID))
            throw new Exception("Bet needs to exist to be removed!");
        betRepository.deleteById(betID);
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
        return direction == null ? betRepository.findAllByGamblerId(gambler_id) :
                betRepository.findAllByGamblerIdSortByDate(gambler_id, direction);
    }

    /**
     * @param gambler_id Identification of the gambler that made the bets
     * @return list of bets of a gambler present in the repository
     * @throws Exception If the gambler does not exist.
     */
    public List<Bet> getGamblerBets(int gambler_id) throws Exception {
        if(!gamblerService.gamblerExistsById(gambler_id))
            throw new Exception("Gambler does not exist!");
        return betRepository.findAllByGamblerId(gambler_id);
    }

    /**
     * Withdraws the winnings of a bet (to nowhere, because it is supposed to simulate the withdrawal to an external account)
     * if all the games have ended and all the game choices are correct
     * @param bet_id Identification of the bet
     * @return transaction of the winnings' withdrawal, or null if at least one game choice is not correct
     * @throws Exception If any error occurs during the withdrawal
     */
    public Transaction withdrawBetWinnings(int bet_id) throws Exception{
        Bet bet = getBet(bet_id);
        if(bet == null)
            throw new Exception("Bet does not exist!");

        if(bet.getState() != Bet.STATE_OPEN)
            throw new Exception("Bet not valid for withdraw!");
        bet.setState(Bet.STATE_CLOSED);

        Transaction bet_transaction = bet.getTransaction();

        float winnings = calculateBetWinnings(bet_transaction.getValue(), bet.getGameChoices(), bet.getCoupon());
        if(winnings == 0) {
            betRepository.save(bet);
            return null;
        }

        Transaction newTransaction = new Transaction(bet_transaction.getGambler().getId(), null, null,
                "Bet Winnings", winnings, bet_transaction.getCoin().getId(), LocalDateTime.now(ZoneId.of("UTC+00:00")));

        betRepository.save(bet);
        return transactionService.addTransaction(newTransaction);
    }

    /**
     * Withdraws the winnings of a bet to a wallet, if all the games have ended and all the game choices are correct
     * @param bet_id Identification of the bet
     * @param wallet_id Identification of the wallet in which the deposit must be done
     * @return transaction of the winnings' withdrawal, or null if at least one game choice is not correct
     * @throws Exception If any error occurs during the withdrawal
     */
    public Transaction withdrawBetWinnings(int bet_id, int wallet_id) throws Exception{
        Bet bet = getBet(bet_id);
        if(bet == null)
            throw new Exception("Bet does not exist!");

        if(bet.getState() != Bet.STATE_OPEN)
            throw new Exception("Bet not valid for withdraw!");
        bet.setState(Bet.STATE_CLOSED);

        Transaction bet_transaction = bet.getTransaction();
        Wallet wallet_withdraw = walletService.getWallet(wallet_id);
        if(wallet_withdraw == null)
            throw new Exception("Wallet chosen for withdraw does not exist!");

        if(bet_transaction.getGambler().getId() != wallet_withdraw.getGambler().getId())
            throw new Exception("Withdraw cannot be performed into a wallet that " +
                    "does not belong to the gambler that placed the bet!");

        if(!bet_transaction.getCoin().getId().equals(wallet_withdraw.getCoin().getId()))
            throw new Exception("Wallet does not use the same coin has the one used to place the bet!");

        float winnings = calculateBetWinnings(bet_transaction.getValue(), bet.getGameChoices(), bet.getCoupon());
        if(winnings == 0) {
            betRepository.save(bet);
            
            Gambler gambler = wallet_withdraw.getGambler();
            String email = gamblerService.getGamblerEmail(gambler.getId());
            String message = "Unfortunately, it seems that you have lost a bet.";
            String subject = "[RASBet] Bet Lost";
            Notification notification = new Notification(gambler.getId(), email, message, subject);
            notificationService.addNotification(notification);

            return null;
        }

        wallet_withdraw = walletService.addToBalance(wallet_id, winnings);
        Transaction newTransaction = new Transaction(bet_transaction.getGambler().getId(), wallet_id, wallet_withdraw.getBalance(),
                "Bet Winnings", winnings, bet_transaction.getCoin().getId(), LocalDateTime.now(ZoneId.of("UTC+00:00")));

        betRepository.save(bet);

        Transaction res = transactionService.addTransaction(newTransaction);

        Gambler gambler = res.getGambler();
        String email = gamblerService.getGamblerEmail(gambler.getId());
        String message = "Congratulations! You just won a bet!";
        String subject = "[RASBet] Bet Won";
        Notification notification = new Notification(gambler.getId(), email, message, subject);
        notificationService.addNotification(notification);

        return res;
    }

    // --------------- Auxiliary Methods ---------------

    /**
     * @param value Value of the bet.
     * @param gameChoices Choices made on the bet.
     * @return 0 if at least 1 game choice is incorrect, or the value that the gambler should receive for winning the bet.
     * @throws Exception If the value is negative or if the game is not in a state that allows withdrawal.
     */
    private float calculateBetWinnings(float value, Collection<GameChoice> gameChoices, String coupon) throws Exception {
        if(gameChoices == null)
            return 0;

        if(value < 0)
            throw new Exception("Amount bet can not be negative!");

        for(GameChoice gc : gameChoices){
            if(gc != null) {
                Game game = gameService.getGame(gc.getGame().getId());
                if (game.getState() != Game.CLOSED)
                    throw new Exception("Game state does not allow withdraw of bet. Check back later!");
                if (game.getWinner_id() == gc.getParticipant().getId())
                    value *= gc.getOdd();
                else
                    return 0;
            }
        }

        //Applying the boost odd promotion
        if(coupon != null){
            var promotion = promotionService.getPromotionByCoupon(coupon);
            if(!(promotion instanceof IBoostOddPromotion boostOddPromotion))
                throw new Exception("Invalid coupon given!");
            value *= 1 + (boostOddPromotion.getBoostOddPercentage() / 100);
        }

        return value;
    }

    /**
     * Validates the game choices present in a bet
     * @param gameChoices Collection of game choices belonging to a bet
     * @throws Exception If the game does not exist, if the game is not open for bets or if the participant does not exist or is not associated with the given game.
     */
    private void validateGameChoices(int gambler_id, Collection<GameChoice> gameChoices) throws Exception {
        if(gameChoices == null || gameChoices.size() == 0)
            throw new Exception("Bet requires at least 1 valid game choice!");

        //Gets all the ids of the games in which the gambler has already placed a bet
        var gamesWithBet = betRepository.findGamblerGameChoices(gambler_id)
                                                     .stream()
                                                     .map(gc -> gc.getGame().getId())
                                                     .collect(Collectors.toSet());

        for(GameChoice gc : gameChoices){
            gc.setId(0); // Certifica q n dá erro por ter sido mencionado um id

            Game game = gc.getGame();
            if(game == null || (game = gameService.getGame(game.getId())) == null)
                throw new Exception("Trying to bet in a non existent game!");

            //Checks if the gambler has already bet in the game
            if(gamesWithBet.contains(game.getId()))
                throw new Exception("Cannot bet multiple times in the same game.");

            if(game.getState() != Game.OPEN)
                throw new Exception("Game with id " + game.getId() + " is not open for bets");

            Participant p = gc.getParticipant();
            if(p == null || !gameService.participantExistsById(p.getId()))
                throw new Exception("One or more participants are invalid!");

            //Adds the game to list of games bet, to avoid betting twice in a game if the list of game choices
            //given as parameter contains a bet to the same game
            gamesWithBet.add(game.getId());
        }
    }
}
package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.GameChoiceRepository;
import ras.adlrr.RASBet.model.Promotions.interfaces.IBoostOddPromotion;
import ras.adlrr.RASBet.service.PromotionServices.ClientPromotionService;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import ras.adlrr.RASBet.dao.BetRepository;
import ras.adlrr.RASBet.model.*;
import ras.adlrr.RASBet.service.interfaces.IBetService;
import ras.adlrr.RASBet.service.interfaces.IGamblerService;
import ras.adlrr.RASBet.service.interfaces.IGameService;
import ras.adlrr.RASBet.service.interfaces.INotificationService;
import ras.adlrr.RASBet.service.interfaces.IParticipantService;
import ras.adlrr.RASBet.service.interfaces.ITransactionService;
import ras.adlrr.RASBet.service.interfaces.IWalletService;
import ras.adlrr.RASBet.service.interfaces.Promotions.IPromotionService;
import ras.adlrr.RASBet.service.interfaces.Promotions.IClientPromotionService;


@Service
public class BetService implements IBetService{
    private final ITransactionService transactionService;
    private final IWalletService walletService;
    private final IGamblerService gamblerService;
    private final IGameService gameService;
    private final IParticipantService participantService;
    private final IClientPromotionService clientPromotionService;
    private final BetRepository betRepository;
    private final GameChoiceRepository gameChoiceRepository;
    private final IPromotionService promotionService;
    private final INotificationService notificationService;

    @Autowired
    public BetService(ITransactionService transactionService, IWalletService walletService,
                      BetRepository betRepository, IGamblerService gamblerService,
                      IGameService gameService, IClientPromotionService clientPromotionService,
                      IPromotionService promotionService, INotificationService notificationService,
                      GameChoiceRepository gameChoiceRepository, IParticipantService participantService) {
        this.transactionService = transactionService;
        this.walletService = walletService;
        this.betRepository = betRepository;
        this.gameChoiceRepository = gameChoiceRepository;
        this.gamblerService = gamblerService;
        this.gameService = gameService;
        this.clientPromotionService = clientPromotionService;
        this.promotionService = promotionService;
        this.participantService = participantService;
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
        participantService.giveOddToGameChoices(gameChoices);

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
        String coupon = bet.getCoupon();
        if (coupon != null)
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
     * Withdraws the winnings of a bet to a wallet, if all the games have ended and all the game choices are correct
     * @param bet_id Identification of the bet
     * @return transaction of the winnings' withdrawal, or null if at least one game choice is not correct
     * @throws Exception If any error occurs during the withdrawal
     */
    @Transactional(rollbackOn = {Exception.class, DataAccessException.class}, value = Transactional.TxType.REQUIRES_NEW)
    public Transaction closeBetAndWithdrawWinnings(int bet_id) throws Exception{
        Bet bet = getBet(bet_id);

        //Checks if the bet is valid
        if(bet == null)
            throw new Exception("Bet does not exist!");

        //Checks if the state is valid for a withdrawal operation
        if(bet.getState() != Bet.STATE_PENDING)
            throw new Exception("Bet not valid for withdraw!");

        Transaction bet_transaction = bet.getTransaction();
        int gambler_id = bet_transaction.getGambler().getId();
        String coin_id = bet_transaction.getCoin().getId();

        //Gets the gambler's wallet that has the same coin has the one used to bet
        Wallet wallet_withdraw = walletService.getWalletByGamblerIdAndCoinId(gambler_id, coin_id);

        //If the gambler does not have a wallet with the specific coin, then a wallet is created
        if(wallet_withdraw == null)
            wallet_withdraw = walletService.createWallet(new Wallet(coin_id, gambler_id));

        //Check if the gambler won the bet and calculates the winnings
        float winnings = calculateBetWinnings(bet_transaction.getValue(), bet.getGameChoices(), bet.getCoupon());

        //If the winnings equal to 0, then the bet was lost
        if(winnings == 0) {
            //updates the bet state to lost
            bet.setState(Bet.STATE_LOST);
            betRepository.save(bet);

            //Notifies the gambler of the lost bet
            Gambler gambler = wallet_withdraw.getGambler();
            String email = gamblerService.getGamblerEmail(gambler.getId());
            String message = "Unfortunately, it seems that you have lost a bet.";
            String subject = "[RASBet] Bet Lost";
            Notification notification = new Notification(gambler.getId(), email, message, subject);
            notificationService.addNotification(notification);

            return null;
        }else {
            //Saves the bet
            bet.setState(Bet.STATE_WON);
            betRepository.save(bet);
        }

        //Adds the winnings to the wallet
        var wallet_id = wallet_withdraw.getId();
        wallet_withdraw = walletService.addToBalance(wallet_id, winnings);

        //Creates the transaction
        Transaction newTransaction = new Transaction(gambler_id, wallet_id, wallet_withdraw.getBalance(),
                "Bet Winnings", winnings, coin_id, LocalDateTime.now(ZoneId.of("UTC+00:00")));
        Transaction res = transactionService.addTransaction(newTransaction);

        //Notifies the gambler that he won the bet
        Gambler gambler = res.getGambler();
        String email = gamblerService.getGamblerEmail(gambler.getId());
        String message = "Congratulations! You just won a bet!";
        String subject = "[RASBet] Bet Won";
        Notification notification = new Notification(gambler.getId(), email, message, subject);
        notificationService.addNotification(notification);

        return res;
    }

    @Transactional
    @Override
    public List<Bet> getBetsByGameId(int game_id) throws Exception {
        if(!gameService.gameExistsById(game_id))
            throw new Exception("Game does not exist!");
        return betRepository.getBetsIdsByGameId(game_id).stream()
                                                        .map(id -> betRepository.findById(id).get())
                                                        .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getBetsIdsByGameId(int game_id) throws Exception{
        if(!gameService.gameExistsById(game_id))
            throw new Exception("Game does not exist!");
        return betRepository.getBetsIdsByGameId(game_id);
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
        var gamesWithBet = gameChoiceRepository.findGamblerGameChoicesIds(gambler_id)
                                                            .stream()
                                                            .map(id -> gameChoiceRepository.findById(id).get())
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
            if(p == null || !participantService.participantExistsById(p.getId()))
                throw new Exception("One or more participants are invalid!");

            Set<Participant> participants = game.getParticipants();
            boolean foundP = false;
            for(Participant participant: participants){
                if (participant.getId() == p.getId()){
                    foundP = true;
                    break;
                }
            }
            if (!foundP)
                throw new Exception("Partipant doesn't belong in game " + game.getTitle() + "!");

            //Adds the game to list of games bet, to avoid betting twice in a game if the list of game choices
            //given as parameter contains a bet to the same game
            gamesWithBet.add(game.getId());
        }
    }
}
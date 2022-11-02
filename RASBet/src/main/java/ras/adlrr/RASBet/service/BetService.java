package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.*;
import ras.adlrr.RASBet.model.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
public class BetService {
    private final TransactionService transactionService;
    private final WalletService walletService;
    private final UserService userService;
    private final GameService gameService;
    private final BetRepository betRepository;

    @Autowired
    public BetService(TransactionService transactionService, WalletService walletService,
                      BetRepository betRepository, UserService userService,
                      GameService gameService) {
        this.transactionService = transactionService;
        this.walletService = walletService;
        this.betRepository = betRepository;
        this.userService = userService;
        this.gameService = gameService;
    }


    // VER SE DA PARA APROVEITAR PARA A PARTE DAS BETS
        /*
        Bet bet = t.getBet();
        if (bet != null) {
            Game game = gameRepository.findById(bet.getGame().getId()).orElse(null);
            assert game != null;
            bet.setGame(game);
            List<GameChoice> gameChoices = bet.getGameChoices().stream().map(gc -> {
                Participant participant = participantRepository.findById(gc.getParticipant().getId()).orElse(null);
                assert participant != null;
                gc.setParticipant(participant);
                return gc;
            }).toList();
            bet.setGameChoices(gameChoices);
            bet.setTransaction(t);
        }
*/

    public Bet getBet(int id) {
        return betRepository.findById(id).orElse(null);
    }

    public Bet addBet(Bet bet) throws Exception {
        if(bet == null)
            throw new Exception("Null Bet!");

        List<GameChoice> gameChoices = bet.getGameChoices();
        if(gameChoices != null)
            gameChoices = gameChoices.stream().filter(Objects::nonNull).toList();
        
        validateGameChoices(gameChoices);

        Transaction transaction = bet.getTransaction();
        if(transaction == null)
            throw new Exception("Null Transaction!");

        Wallet wallet = transaction.getWallet();
        if(wallet != null) {
            wallet = walletService.removeFromBalance(wallet.getId(), transaction.getValue());
            transaction.setWallet(wallet);
            transaction.setBalance_after_mov(wallet.getBalance());
        }

        transaction.setDescription("Bet expenses");
        transaction = transactionService.addTransaction(transaction);
        bet.setTransaction(transaction);
        bet.setId(transaction.getId());

        return betRepository.save(bet);
    }

    public void removeBet(int betID) throws Exception {
        if(!betRepository.existsById(betID))
            throw new Exception("Bet needs to exist to be removed!");
        betRepository.deleteById(betID);
    }

    public List<Bet> getGamblerBets(int gambler_id) throws Exception {
        if(!userService.gamblerExistsById(gambler_id))
            throw new Exception("Gambler does not exist!");
        return betRepository.findAllByGamblerId(gambler_id);
    }

    //Withdraw the winnings directly into a bank account, paypal, etc...
    public void withdrawBetWinnings() throws Exception{
        //TODO
    }

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

        float winnings = calculateBetWinnings(bet_transaction.getValue(), bet.getGameChoices());
        if(winnings == 0) {
            betRepository.save(bet);
            return null;
        }

        wallet_withdraw = walletService.addToBalance(wallet_id, winnings);
        Transaction newTransaction = new Transaction(wallet_withdraw.getGambler().getId(), wallet_id, wallet_withdraw.getBalance(),
                "Bet Winnings", winnings, LocalDateTime.now());

        betRepository.save(bet);
        return transactionService.addTransaction(newTransaction);
    }

    //Assuming value is positive
    private float calculateBetWinnings(float value, Collection<GameChoice> gameChoices) throws Exception {
        if(gameChoices == null)
            return 0;

        if(value < 0)
            throw new Exception("Amount betted can not be negative!");

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

        return value;
    }

    // --------------- Auxiliary Methods ---------------
    
    private void validateGameChoices(Collection<GameChoice> gameChoices) throws Exception {
        if(gameChoices == null || gameChoices.size() == 0)
            throw new Exception("Bet requires at least 1 valid game choice!");

        for(GameChoice gc : gameChoices){
            gc.setId(0); // Certifica q n dá erro por ter sido mencionado um id

            Game game = gc.getGame();
            if(!gameService.gameExistsById(game.getId()))
                throw new Exception("Trying to bet in a non existent game!");

            if(gc.getOdd() < 1)
                throw new Exception("Odds must be equal or higher to/than 1!");

            Participant p = gc.getParticipant();
            if(p == null || !gameService.participantExistsById(p.getId()))
                throw new Exception("One or more participants are invalid!");
        }
    }
}
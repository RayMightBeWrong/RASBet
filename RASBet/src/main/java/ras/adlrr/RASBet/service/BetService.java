package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.*;
import ras.adlrr.RASBet.model.*;

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
            wallet = walletService.performBilling(wallet.getId(), transaction.getValue());
            transaction.setWallet(wallet);
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

    public void withdrawBetWinnings(int bet_id, int wallet_id) throws Exception{
        Bet bet = betRepository.findById(bet_id).orElse(null);
        if(bet == null)
            throw new Exception("Bet does not exist!");
        bet.setState(Bet.STATE_CLOSED);

        Wallet wallet = walletService.getWallet(wallet_id);

        //TODO
    }

    // --------------- Auxiliar Methods ---------------
    
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
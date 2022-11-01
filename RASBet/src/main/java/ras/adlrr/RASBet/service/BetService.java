package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.dao.*;
import ras.adlrr.RASBet.model.*;

import java.util.List;
import java.util.Objects;

@Service
public class BetService {
    private final TransactionService transactionService;
    private final WalletService walletService;
    private final BetRepository betRepository;
    private final GamblerRepository gamblerRepository;
    private final ParticipantRepository participantRepository;
    private final GameRepository gameRepository;

    @Autowired
    public BetService(TransactionService transactionService, WalletService walletService, BetRepository betRepository, ParticipantRepository participantRepository, GamblerRepository gamblerRepository, GameRepository gameRepository) {
        this.transactionService = transactionService;
        this.walletService = walletService;
        this.betRepository = betRepository;
        this.participantRepository = participantRepository;
        this.gamblerRepository = gamblerRepository;
        this.gameRepository = gameRepository;
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
        if(gameChoices == null || gameChoices.stream().noneMatch(Objects::nonNull))
            throw new Exception("Bet requires at least 1 valid game choice!");

        for(GameChoice gc : gameChoices.stream().filter(Objects::nonNull).toList()){
            gc.setId(0); // Certifica q n dá erro por ter sido mencionado um id

            Game game = gc.getGame();
            if(!gameRepository.existsById(game.getId()))
                throw new Exception("Trying to bet in a non existent game!");

            if(gc.getOdd() < 1)
                throw new Exception("Odds must be equal or higher to/than 1!");

            Participant p = gc.getParticipant();
            if(p == null || !participantRepository.existsById(p.getId()))
                throw new Exception("One or more participants are invalid!");
        }

        Transaction transaction = bet.getTransaction();
        if(transaction == null)
            throw new Exception("Null Transaction!");

        Wallet wallet = transaction.getWallet();
        if(wallet != null) {
            wallet = walletService.performBilling(wallet.getId(), transaction.getValue());
            transaction.setWallet(wallet);
        }

        transaction.setDescription("Bet");
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
        if(!gamblerRepository.existsById(gambler_id))
            throw new Exception("Gambler does not exist!");
        return betRepository.findAllByGamblerId(gambler_id);
    }
}
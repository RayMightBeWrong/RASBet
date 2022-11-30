package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ras.adlrr.RASBet.model.*;
import ras.adlrr.RASBet.service.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

@RestController
@RequestMapping("/seed")
public class SeedController {

    private final BetService betService;
    private final GameService gameService;
    private final SportService sportService;
    private final WalletService walletService;
    private final TransactionService transactionService;
    private final UserService userService;

    @Autowired
    public SeedController(BetService betService, GameService gameService, SportService sportService, WalletService walletService, TransactionService transactionService, UserService userService) {
        this.betService = betService;
        this.gameService = gameService;
        this.sportService = sportService;
        this.walletService = walletService;
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @PostMapping
    public void seed() throws Exception {
        //Create gambler
        Gambler gambler = new Gambler(0, "Gambler", "gambler@gmail.com", "1234", "12345678", 123456789,
                LocalDate.now(ZoneId.of("UTC+00:00")).minusYears(18), 123456789, "Portugal", "Braga", "Rua", "4705-651", "Student");
        gambler = userService.addGambler(gambler);

        //Create admin
        Admin admin = new Admin(0, "Admin", "1234", "admin@gmail.com");
        admin = userService.addAdmin(admin);

        //Create expert
        Expert expert = new Expert(0, "Expert", "1234", "expert@gmail.com");
        expert = userService.addExpert(expert);

        //Create coins
        Coin EUR = new Coin("EUR", 1);
        Coin Bitcoin = new Coin("Bitcoin", 20500);
        EUR = walletService.addCoin(EUR);
        Bitcoin = walletService.addCoin(Bitcoin);

        //Create wallets for gambler
        Wallet walletEUR = new Wallet("EUR", gambler.getId());
        walletEUR = walletService.createWallet(walletEUR);
        Wallet walletBitcoin = new Wallet("Bitcoin", gambler.getId());
        walletBitcoin = walletService.createWallet(walletBitcoin);

        //Create sports
        Sport football = new Sport("Football", Sport.WITH_DRAW);
        Sport nfl = new Sport("NFL", Sport.WITH_DRAW);
        Sport F1 = new Sport("F1", Sport.RACE);
        Sport NBA = new Sport("NBA", Sport.WITHOUT_DRAW);
        football = sportService.addSport(football);
        nfl = sportService.addSport(nfl);
        F1 = sportService.addSport(F1);
        NBA = sportService.addSport(NBA);

        gameService.addGame(new Game("EXTID", LocalDateTime.now().plusDays(2), Game.OPEN, "Alex BC vs Luis BC",
                "Football", Set.of(new Participant("Alex BC", 1, 1000), new Participant("Luis BC", 1000, 0))));
        //Update games with football api
        gameService.updateGamesVPN();
        //gameService.updateGames();
    }
}

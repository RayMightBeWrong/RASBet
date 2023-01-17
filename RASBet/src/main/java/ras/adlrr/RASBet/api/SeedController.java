package ras.adlrr.RASBet.api;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ras.adlrr.RASBet.model.*;
import ras.adlrr.RASBet.service.interfaces.IUserReferralService;
import ras.adlrr.RASBet.service.interfaces.balance.ICoinService;
import ras.adlrr.RASBet.service.interfaces.balance.IWalletService;
import ras.adlrr.RASBet.service.interfaces.bets.IBetService;
import ras.adlrr.RASBet.service.interfaces.sports.IGameService;
import ras.adlrr.RASBet.service.interfaces.sports.ISportService;
import ras.adlrr.RASBet.service.interfaces.transactions.ITransactionService;
import ras.adlrr.RASBet.service.interfaces.users.IAdminService;
import ras.adlrr.RASBet.service.interfaces.users.IExpertService;
import ras.adlrr.RASBet.service.interfaces.users.IGamblerService;
import ras.adlrr.RASBet.service.interfaces.users.IUserService;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Set;

@RestController
@RequestMapping("/seed")
public class SeedController {

    private final IBetService betService;
    private final IGameService gameService;
    private final ISportService sportService;
    private final IWalletService walletService;
    private final ICoinService coinService;
    private final ITransactionService transactionService;
    private final IUserService userService;
    private final IGamblerService gamblerService;
    private final IAdminService adminService;
    private final IExpertService expertService;
    private final IUserReferralService userReferralService;

    public SeedController(@Qualifier("betGameFacade") IBetService betService,
                          @Qualifier("sportsFacade") IGameService gameService,
                          @Qualifier("sportsFacade") ISportService sportService,
                          @Qualifier("balanceFacade") IWalletService walletService,
                          @Qualifier("balanceFacade") ICoinService coinService,
                          @Qualifier("transactionFacade") ITransactionService transactionService,
                          @Qualifier("userFacade") IUserService userService,
                          @Qualifier("userFacade") IGamblerService gamblerService,
                          @Qualifier("userFacade") IAdminService adminService,
                          @Qualifier("userFacade") IExpertService expertService,
                          @Qualifier("userReferralService") IUserReferralService userReferralService) {
        this.betService = betService;
        this.gameService = gameService;
        this.sportService = sportService;
        this.walletService = walletService;
        this.coinService = coinService;
        this.transactionService = transactionService;
        this.userService = userService;
        this.gamblerService = gamblerService;
        this.adminService = adminService;
        this.expertService = expertService;
        this.userReferralService = userReferralService;
    }

    @PostMapping
    public void seed() throws Exception {
        //Create gambler
        Gambler gambler = new Gambler(0, "Gambler", "gambler@gmail.com", "1234", "11111111", 111111111,
                LocalDate.now(ZoneId.of("UTC+00:00")).minusYears(18), 111111111, "Portugal", "Braga", "Rua", "4705-651", "Student");
        gambler = gamblerService.addGambler(gambler);
        Gambler gambler2 = new Gambler(0, "Gambler2", "gambler2@gmail.com", "1234", "22222222", 222222222,
                LocalDate.now(ZoneId.of("UTC+00:00")).minusYears(19), 222222222, "Portugal", "Porto", "Rua 2", "4800-500", "Cook");
        gambler2 = userReferralService.createGambler(gambler.getId(), gambler2);

        //Create admin
        Admin admin = new Admin(0, "Admin", "1234", "admin@gmail.com");
        admin = adminService.addAdmin(admin);

        //Create expert
        Expert expert = new Expert(0, "Expert", "1234", "expert@gmail.com");
        expert = expertService.addExpert(expert);

        //Create coins
        Coin EUR = new Coin("EUR", 1);
        Coin Bitcoin = new Coin("Bitcoin", 20500);
        EUR = coinService.addCoin(EUR);
        Bitcoin = coinService.addCoin(Bitcoin);

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
        gameService.updateGames();
    }
}

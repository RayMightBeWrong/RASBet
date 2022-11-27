package ras.adlrr.RASBet.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ras.adlrr.RASBet.service.interfaces.IBetGameService;
import ras.adlrr.RASBet.service.interfaces.IBetService;
import ras.adlrr.RASBet.service.interfaces.IGameService;

@Service
@RequiredArgsConstructor
public class BetGameService implements IBetGameService {
    private final IBetService betService;
    private final IGameService gameService;

    @Override
    public void closeGameAndWithdrawBets(int game_id) throws Exception {
        //Closes game
        gameService.closeGame(game_id);

        //Gets game's list of bets
        var bets = betService.getBetsIdsByGameId(game_id);

        //Withdraws the bets that can be withdrawn
        for(Integer bet_id : bets){
            try {
                betService.closeBetAndWithdrawWinnings(bet_id);
            }catch (Exception ignored){}
        }
    }
}

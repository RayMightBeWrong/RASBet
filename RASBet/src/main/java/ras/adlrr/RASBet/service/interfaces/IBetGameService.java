package ras.adlrr.RASBet.service.interfaces;

public interface IBetGameService {
    void closeGameAndWithdrawBets(int game_id) throws Exception;
    public void updateGames() throws Exception;
}

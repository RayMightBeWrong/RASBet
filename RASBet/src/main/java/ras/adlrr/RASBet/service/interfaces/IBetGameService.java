package ras.adlrr.RASBet.service.interfaces;

public interface IBetGameService {
    void closeGameAndWithdrawBets(int game_id) throws Exception;
    void updateGames() throws Exception;
    void editOddInParticipant(int participant_id, float odd) throws Exception;
    void editScoreInParticipant(int participant_id, int score) throws Exception;
    void changeGameState(int id, int state) throws Exception;
}

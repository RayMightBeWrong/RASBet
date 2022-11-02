package ras.adlrr.RASBet.UI.ModelViews;

import ras.adlrr.RASBet.model.Bet;
import ras.adlrr.RASBet.model.GameChoice;

public class BetView {
    private Bet bet;

    public BetView(Bet bet) {
        this.bet = bet;
    }

    public String toString(){
        String s = "   BET\n";
        s += "Apostas: \n";
        for (GameChoice gameChoice: bet.getGameChoices()){
            s += "  Escolha: " + gameChoice.getParticipant().getName()+ "\n";
            s += "  Resultado: " + gameChoice.getParticipant().getScore()+ "\n";
            s += "  Odd: " + gameChoice.getOdd() + "\n";
        }
        TransactionView transactionView = new TransactionView(bet.getTransaction());
        s += transactionView.toString();
        return s;
    }
}

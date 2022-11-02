package ras.adlrr.RASBet.UI.ModelViews;

import ras.adlrr.RASBet.model.Participant;

public class ParticipantView {
    private Participant participant;

    public ParticipantView(Participant participant) {
        this.participant = participant;
    }

    public String toString(){
        return "Choice = " + participant.getName() + " | Odd = " + participant.getOdd();
    }
}

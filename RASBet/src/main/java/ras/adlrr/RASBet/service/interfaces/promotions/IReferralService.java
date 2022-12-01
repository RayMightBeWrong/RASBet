package ras.adlrr.RASBet.service.interfaces.promotions;

import java.time.LocalDateTime;

public interface IReferralService {
    public void addReferral(int referredId, int referrerId) throws Exception;

    public int getGamblerNrOfReferrals(int gambler_id);

    public int getGamblerNrOfReferralsBetweenDates(int gambler_id, LocalDateTime initial_date, LocalDateTime end_date);
}

package ras.adlrr.RASBet.model.Promotions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ras.adlrr.RASBet.model.Gambler;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "referrals")
public class Referral {
    @Embeddable
    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    public static class ReferralID implements Serializable {
        private int referredId;
        private int referrerId;
    }

    @EmbeddedId
    private ReferralID id;

    @MapsId("referredId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "referred_id", updatable = false, insertable = false, nullable = false)
    private Gambler referred;

    @MapsId("referrerId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "referrer_id", updatable = false, insertable = false, nullable = false)
    private Gambler referrer;

    @Column(nullable = false)
    private LocalDateTime registration_date;

    public Referral(int referredId, int referrerId, LocalDateTime registration_date){
        this.id = new ReferralID(referredId, referrerId);
        this.registration_date = registration_date;
        this.referred = new Gambler(); this.referred.setId(referredId);
        this.referrer = new Gambler(); this.referrer.setId(referrerId);
    }
}

package ras.adlrr.RASBet.model.Promotions;

import ras.adlrr.RASBet.model.Gambler;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;


@Entity
@Table(name = "referrals")
public class Referral {
    @Embeddable
    public static class ReferralID implements Serializable {
        public int referredId;
        public int referrerId;
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
}

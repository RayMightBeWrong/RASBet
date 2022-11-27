package ras.adlrr.RASBet.model.Promotions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ras.adlrr.RASBet.model.Gambler;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;


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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ReferralID that = (ReferralID) o;
            return referredId == that.referredId && referrerId == that.referrerId;
        }

        @Override
        public int hashCode() {
            return Objects.hash(referredId, referrerId);
        }
    }

    @EmbeddedId
    private ReferralID id;

    @MapsId("referredId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "referred_id", updatable = false, nullable = false)
    private Gambler referred;

    @MapsId("referrerId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "referrer_id", updatable = false, nullable = false, unique = true)
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

package ras.adlrr.RASBet.model.Promotions;

import java.time.LocalDateTime;

public interface IPromotion {
    int getId();
    String getTitle();
    String getDescription();
    LocalDateTime getBegin_date();
    LocalDateTime getExpiration_date();
    int getNr_uses();
    String getCoupon();

    void setId(int id);
    void setTitle(String title);
    void setDescription(String description);
    void setBegin_date(LocalDateTime begin_date);
    void setExpiration_date(LocalDateTime expiration_date);
    void setNr_uses(int nr_uses);
    void setCoupon(String coupon);
}

package ras.adlrr.RASBet.model.Promotions.interfaces;

import java.time.LocalDateTime;

public interface IPromotion {
    int getId();
    String getTitle();
    String getDescription();
    LocalDateTime getBeginDate();
    LocalDateTime getExpirationDate();
    int getNr_uses();
    String getCoupon();

    void setId(int id);
    void setTitle(String title);
    void setDescription(String description);
    void setBeginDate(LocalDateTime begin_date);
    void setExpirationDate(LocalDateTime expiration_date);
    void setNr_uses(int nr_uses);
    void setCoupon(String coupon);
}

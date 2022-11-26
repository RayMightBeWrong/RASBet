package ras.adlrr.RASBet.model.Promotions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ras.adlrr.RASBet.model.Promotions.interfaces.IPromotion;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "promotions")
public class Promotion implements IPromotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false, name = "begin_date")
    private LocalDateTime begin_date;

    @Column(name = "expiration_date")
    private LocalDateTime expiration_date;

    private int nr_uses; //nr of uses allowed for the coupon generated

    @Column(unique = true)
    private String coupon;

    public Promotion(@JsonProperty("title") String title, @JsonProperty("description") String description,
                     @JsonProperty("begin_date") LocalDateTime begin_date, @JsonProperty("expiration_date") LocalDateTime expiration_date,
                     @JsonProperty("nr_uses") int nr_uses) {
        this.title = title;
        this.description = description;
        this.begin_date = begin_date;
        this.expiration_date = expiration_date;
        this.nr_uses = nr_uses;
    }
}

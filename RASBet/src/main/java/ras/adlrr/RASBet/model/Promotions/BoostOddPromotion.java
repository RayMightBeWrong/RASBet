package ras.adlrr.RASBet.model.Promotions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "boost_odd_promotions")
public class BoostOddPromotion extends Promotion{
    private float boost_percentage;

    public BoostOddPromotion(@JsonProperty("title") String title, @JsonProperty("description") String description,
                             @JsonProperty("beginDate") LocalDateTime beginDate, @JsonProperty("expirationDate") LocalDateTime expirationDate,
                             @JsonProperty("nr_uses") int nr_uses, @JsonProperty("boost_percentage") float boost_percentage) {
        super(title, description, beginDate, expirationDate, nr_uses);
        this.boost_percentage = boost_percentage;
    }
}

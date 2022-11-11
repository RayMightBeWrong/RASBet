package ras.adlrr.RASBet.model.Promotions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "boost_odd_promotions")
public class BoostOddPromotion extends Promotion{
    private int nr_uses; //nr of uses allowed for the coupon generated
    private float boost_percentage;
}

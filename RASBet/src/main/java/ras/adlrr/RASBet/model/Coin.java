package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "coin")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Coin {
    @Id
    private String id;
    private float ratio_EUR;

    public String toString(){
        return id + " | Ratio de euro: " + ratio_EUR;
    }
}

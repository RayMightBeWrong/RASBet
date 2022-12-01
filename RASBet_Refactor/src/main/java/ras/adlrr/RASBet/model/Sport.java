package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "sport")
public class Sport {
    @Id
    private String id;
    private int type; //Type of sport (E.g.: Collective and without draw / Non collective and with draw ...)

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sport", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Game> games;

    public static final int WITH_DRAW = 1;
    public static final int WITHOUT_DRAW = 2;
    public static final int RACE = 3;

    public Sport(@JsonProperty("id") String id, @JsonProperty("type") int type){
        this.id = id;
        this.type = type;
    }
}

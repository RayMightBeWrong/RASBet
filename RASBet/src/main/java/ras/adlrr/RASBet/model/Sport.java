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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id; //Unique identifier of the sport

    @Column(name = "name", unique = true)
    private String name;

    private int type; //Type of sport (E.g.: Collective and without draw / Non collective and with draw ...)

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sport", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Game> games;

    public static final int WITH_DRAW = 1;
    public static final int WITHOUT_DRAW = 2;
    public static final int RACE = 3;

    public Sport(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("type") int type){
        this.id = id;
        this.name = name;
        this.type = type;
    }
}

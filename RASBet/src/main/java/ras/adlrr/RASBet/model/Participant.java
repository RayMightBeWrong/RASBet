package ras.adlrr.RASBet.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "participants")
@NoArgsConstructor
@Getter
@Setter
public class Participant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    private String name;
    private float odd;
    private int score;

    public Participant(@JsonProperty("id") int id, @JsonProperty("name") String name, @JsonProperty("odd") float odd, @JsonProperty("score") int score) {
        this.id = id;
        this.name = name;
        this.odd = odd;
        this.score = score;
    }

    public Participant(String name, float odd, int score) {
        this.name = name;
        this.odd = odd;
        this.score = score;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Participant that = (Participant) o;
        return id == that.id && Float.compare(that.odd, odd) == 0 && score == that.score && name.equals(that.name);
    }
}

package ras.adlrr.RASBet.model.Promotions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private LocalDateTime beginDate;

    private LocalDateTime expirationDate;

    public Promotion(@JsonProperty("title") String title,@JsonProperty("description") String description,
                     @JsonProperty("beginDate") LocalDateTime beginDate, @JsonProperty("expirationDate") LocalDateTime expirationDate) {
        this.title = title;
        this.description = description;
        this.beginDate = beginDate;
        this.expirationDate = expirationDate;
    }
}

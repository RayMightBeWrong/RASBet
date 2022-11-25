package ras.adlrr.RASBet.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonPropertyOrder({"id","gambler_id","message","message_type","send_type"})
@Table(name = "notification")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "gambler_id", updatable = false, nullable = false)
    @JsonIncludeProperties("id")
    private Gambler gambler;

    private String destEmail;

    private String message;
    private String subject;

    private int messageType;

    public static final int NO_TYPE = 0;
    public static final int ACCOUNT_CREATED = 1;
    public static final int BET_OVER = 2;

    public Notification(@JsonProperty("gambler_id") int gambler_id, @JsonProperty("dest_email") String email, @JsonProperty("message_type") int message_type){
        Gambler gambler = new Gambler();
        gambler.setId(gambler_id);
        this.setGambler(gambler);
        this.setDestEmail(email);
        this.setMessageType(message_type);
        setContent();
    }

    public Notification(int gambler_id, String email, String message, String subject){
        Gambler gambler = new Gambler();
        gambler.setId(gambler_id);
        this.setGambler(gambler);
        this.setDestEmail(email);
        this.setMessageType(NO_TYPE);
        this.setMessage(message);
        this.setSubject(subject);
    }

    public void setContent(){
        switch(this.messageType){
            case ACCOUNT_CREATED:
                this.message = "Congratulations! You just created an account at RASBet";
                this.subject = "[RASBet] Created Account at RASBet";
                break;

            case BET_OVER:
                this.message = "Your bet is over";
                this.subject = "[RASBet] BET OVER";
                break;
        }   
    }
}

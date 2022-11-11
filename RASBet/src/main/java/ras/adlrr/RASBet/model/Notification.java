package ras.adlrr.RASBet.model;

import java.util.Properties;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.fasterxml.jackson.annotation.JsonIncludeProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "notification")
@NoArgsConstructor
@Getter
@Setter
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "gambler_id", updatable = false, nullable = false)
    @JsonIncludeProperties("id")
    private Gambler gambler;

    private String message;

    private int messageType;

    public void sendEmail(){
        MailSender ms = getJavaMailSender();

        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("rasbet.ucras@gmail.com");
        message.setTo(""); 
        
        message.setSubject("Uma Beca Cringe"); 
        message.setText("Aposto que ainda não ouviste a música que te mandei ouvir\n\nhttps://www.youtube.com/watch?v=kr4DNZz_8zI");
        ms.send(message);
    }

    @Bean
    public MailSender getJavaMailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setHost("smtp.gmail.com");
        mailSender.setPort(587);
    
        mailSender.setUsername("rasbet.ucras@gmail.com");
        mailSender.setPassword("lfzgzdobneohqjzs");
    
        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");
    
        return mailSender;
    }
}

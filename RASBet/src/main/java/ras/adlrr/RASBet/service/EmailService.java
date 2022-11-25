package ras.adlrr.RASBet.service;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import ras.adlrr.RASBet.service.interfaces.IEmailService;


@Service
public class EmailService implements IEmailService {

    public EmailService(){}
    
    public void sendEmail(String dest, String subject, String text){
        MailSender ms = getJavaMailSender();

        SimpleMailMessage message = new SimpleMailMessage(); 
        message.setFrom("rasbet.ucras@gmail.com");
        message.setTo(dest); 
        
        message.setSubject(subject); 
        message.setText(text);
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

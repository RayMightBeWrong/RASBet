package ras.adlrr.RASBet.service;

import org.springframework.mail.MailSender;

public interface IEmailService {
    public void sendEmail(String dest, String subject, String text);

    public MailSender getJavaMailSender();
}

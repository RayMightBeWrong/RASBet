package ras.adlrr.RASBet.service.interfaces;

import org.springframework.mail.MailSender;

public interface IEmailService {
    public void sendEmail(String dest, String subject, String text);

    public MailSender getJavaMailSender();
}

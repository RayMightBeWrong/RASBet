package ras.adlrr.RASBet.service.notifications;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import ras.adlrr.RASBet.dao.NotificationRepository;
import ras.adlrr.RASBet.model.Notification;
import ras.adlrr.RASBet.service.interfaces.notifications.IEmailService;
import ras.adlrr.RASBet.service.interfaces.notifications.INotificationService;

@Service("notificationService")
public class NotificationService implements INotificationService{
    private final NotificationRepository nr;
    private final IEmailService emailService;

    @Autowired
    public NotificationService(NotificationRepository nr, IEmailService emailService){
        this.nr = nr;
        this.emailService = emailService;
    }

    public Notification getNotification(int id){
        return nr.findById(id).orElse(null);
    }

    @Async
    public Notification addNotification(Notification notification) throws Exception{
        if(notification == null)
            throw new Exception("Null Notification!");

        String subject = notification.getSubject();
        String message = notification.getMessage();
        String destMail = notification.getDestEmail();

        emailService.sendEmail(destMail, subject, message);
        return nr.save(notification);
    }

    public void removeNotification(int id) throws Exception {
        if(!nr.existsById(id))
            throw new Exception("Notification needs to exist to be removed!");
        nr.deleteById(id);
    }

    public List<Notification> getNotifications() {
        return nr.findAll();
    }
}

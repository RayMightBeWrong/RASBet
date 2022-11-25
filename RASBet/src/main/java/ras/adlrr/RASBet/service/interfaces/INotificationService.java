package ras.adlrr.RASBet.service.interfaces;

import java.util.List;

import ras.adlrr.RASBet.model.Notification;

public interface INotificationService {
    public Notification getNotification(int id);

    public Notification addNotification(Notification notification) throws Exception;

    public void removeNotification(int id) throws Exception;

    public List<Notification> getNotifications();
}

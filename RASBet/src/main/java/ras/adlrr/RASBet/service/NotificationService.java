package ras.adlrr.RASBet.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ras.adlrr.RASBet.dao.NotificationRepository;

@Service
public class NotificationService implements INotificationService{
    private final NotificationRepository nr;

    @Autowired
    public NotificationService(NotificationRepository nr){
        this.nr = nr;
    }
}

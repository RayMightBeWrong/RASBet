package ras.adlrr.RASBet.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ras.adlrr.RASBet.api.auxiliar.ResponseEntityBadRequest;
import ras.adlrr.RASBet.model.Notification;
import ras.adlrr.RASBet.service.IEmailService;
import ras.adlrr.RASBet.service.INotificationService;

@RequestMapping("/api/notifications")
@RestController
public class NotificationController {
    private final INotificationService notificationService;

    @Autowired
    public NotificationController(INotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Notification> getNotification(@PathVariable("id") int id) {
        return ResponseEntity.ok().body(notificationService.getNotification(id));
    }

    @PostMapping
    public ResponseEntity<Notification> addNotification(@RequestBody Notification notification) {
        try{ 
            return ResponseEntity.ok().body(notificationService.addNotification(notification)); }
        catch (Exception e){
            return new ResponseEntityBadRequest<Notification>().createBadRequest(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity removeNotification(@PathVariable("id") int id) {
        try {
            notificationService.removeNotification(id);
            return new ResponseEntity(HttpStatus.OK); }
        catch (Exception e){
            return new ResponseEntityBadRequest().createBadRequest(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<Notification>> getNotifications() {
        return ResponseEntity.ok().body(notificationService.getNotifications());
    }
}

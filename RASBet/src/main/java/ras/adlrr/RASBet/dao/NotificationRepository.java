package ras.adlrr.RASBet.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ras.adlrr.RASBet.model.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}

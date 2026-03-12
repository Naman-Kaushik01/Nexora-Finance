package com.Nexora.NexoraFinance.notification.repo;

import com.Nexora.NexoraFinance.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepo extends JpaRepository<Notification, Long> {

}

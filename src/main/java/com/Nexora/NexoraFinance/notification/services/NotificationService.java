package com.Nexora.NexoraFinance.notification.services;

import com.Nexora.NexoraFinance.auth_users.entity.User;
import com.Nexora.NexoraFinance.notification.dtos.NotificationDTO;

public interface NotificationService {
    void sendEmail(NotificationDTO notificationDTO , User user);
}

package com.Nexora.NexoraFinance.notification.services;

import com.Nexora.NexoraFinance.auth_users.entity.User;
import com.Nexora.NexoraFinance.notification.dtos.NotificationDTO;
import com.Nexora.NexoraFinance.notification.repo.NotificationRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepo notificationRepo;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Override
    @Async
    public void sendEmail(NotificationDTO notificationDTO, User user) {

    }
}

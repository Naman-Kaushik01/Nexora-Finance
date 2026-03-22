package com.Nexora.NexoraFinance.notification.entity;

import com.Nexora.NexoraFinance.auth_users.entity.User;
import com.Nexora.NexoraFinance.enums.NotificationType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private String recipient;

    private String body;

    @Enumerated(EnumType.STRING)
    private NotificationType type; // EMAIL , SMS , PUSH

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private final LocalDateTime createdAt = LocalDateTime.now();

}

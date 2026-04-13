package com.Nexora.NexoraFinance;

import com.Nexora.NexoraFinance.auth_users.entity.User;
import com.Nexora.NexoraFinance.enums.NotificationType;
import com.Nexora.NexoraFinance.notification.dtos.NotificationDTO;
import com.Nexora.NexoraFinance.notification.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EnableAsync
//@RequiredArgsConstructor
public class NexoraFinanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NexoraFinanceApplication.class, args);
	}


    //Testing mail sender

//    private final NotificationService notificationService;
//
//    @Bean
//    CommandLineRunner runner(){
//        return args -> {
//            NotificationDTO notificationDTO = NotificationDTO.builder()
//                    .recipient("nutanpandey081@gmail.com")
//                    .subject("Nexora Finance")
//                    .body("Hey this is a test email")
//                    .type(NotificationType.EMAIL)
//                    .build();
//            notificationService.sendEmail(notificationDTO , new User());
//        };
//    }


}

package com.Nexora.NexoraFinance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class NexoraFinanceApplication {

	public static void main(String[] args) {
		SpringApplication.run(NexoraFinanceApplication.class, args);

	}

}

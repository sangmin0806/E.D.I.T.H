package com.edith.developmentassistant;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class DevelopmentassistantApplication {

	public static void main(String[] args) {
		SpringApplication.run(DevelopmentassistantApplication.class, args);
	}
}

package com.withdev.orcamentos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OrcamentosApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrcamentosApplication.class, args);
	}

}

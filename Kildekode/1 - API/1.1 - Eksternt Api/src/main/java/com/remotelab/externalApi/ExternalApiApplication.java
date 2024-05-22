package com.remotelab.externalApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ExternalApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ExternalApiApplication.class, args);
	}

}

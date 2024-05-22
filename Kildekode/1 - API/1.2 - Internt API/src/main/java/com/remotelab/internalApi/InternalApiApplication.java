package com.remotelab.internalApi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InternalApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(InternalApiApplication.class, args);
	}

}

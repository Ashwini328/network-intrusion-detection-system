package com.ashwini.idsproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class IdsprojectApplication {

	public static void main(String[] args) {
		SpringApplication.run(IdsprojectApplication.class, args);
	}

}

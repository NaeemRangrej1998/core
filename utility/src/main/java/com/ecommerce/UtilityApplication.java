package com.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ecommerce.*")
public class UtilityApplication {

	public static void main(String[] args) {
		SpringApplication.run(UtilityApplication.class, args);
	}

}

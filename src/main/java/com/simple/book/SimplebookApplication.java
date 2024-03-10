package com.simple.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
public class SimplebookApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimplebookApplication.class, args);
	}

}

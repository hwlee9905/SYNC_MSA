package com.simple.book;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SimplebookApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimplebookApplication.class, args);
	}

}

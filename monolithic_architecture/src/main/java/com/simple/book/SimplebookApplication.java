package com.simple.book;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.simple.book.domain.log.service.LogService;

@EnableJpaAuditing
@EnableScheduling
@SpringBootApplication
public class SimplebookApplication implements ApplicationRunner{
	
	@Autowired
	private LogService logService;
	
	private static boolean hasExecuted = false;

	public static void main(String[] args) {
		SpringApplication.run(SimplebookApplication.class, args);
	}
	
	@Override
    public void run(ApplicationArguments args) throws Exception {
        if (!hasExecuted) {
        	logService.readLog();
            hasExecuted = true;
        }
    }

}

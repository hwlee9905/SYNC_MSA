package com.simple.book.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
	@Value("${directory.path.image}")
	private String imagePath;
	
	@Value("${spring.mail.username}")
	private String mailId;
	
	public String getImagePath() {
		return imagePath;
	}
	
	public String getMailId() {
		return mailId;
	}
	
}

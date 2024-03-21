package com.simple.book.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig {
	@Value("${directory.path.image}")
	private String imagePath;
	
	public String getImagePath() {
		return imagePath;
	}
	
}

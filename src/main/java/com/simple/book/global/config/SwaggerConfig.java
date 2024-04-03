package com.simple.book.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class SwaggerConfig {
	@Bean
    public OpenAPI openAPI() {
        Info info = new Info()
				.title("Hinc!")
				.description("SNS 웹 사이트 Hinc!의 API입니다.")
				.contact(new Contact()
						.name("MinKyeong")
						.url("https://github.com/str0ngMK")
						.email("qldrhsmszz@naver.com, "))
				.contact(new Contact()
						.name("HyunWoong")
						.url("https://github.com/hwlee9905")
						.email("9905hyun@naver.com")
				);
		return new OpenAPI().components(new Components()).info(info);
	}
}

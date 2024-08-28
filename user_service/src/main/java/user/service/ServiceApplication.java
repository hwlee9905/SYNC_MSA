package user.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class ServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServiceApplication.class, args);
	}
	
	@Bean
    public ResourceBundleMessageSource messageSource() {
      ResourceBundleMessageSource source = new ResourceBundleMessageSource();
      source.setBasenames("messages.failure");
      source.setDefaultEncoding("UTF-8");
      return source;
    }
}

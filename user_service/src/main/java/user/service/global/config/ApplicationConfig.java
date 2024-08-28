package user.service.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
public class ApplicationConfig {
	@Value("${spring.kafka.bootstrap-servers}")
	private String kafkaHost;

	@Value("${api.server.alarm}")
	private String alarmApi;

	@Value("${api.server.project}")
	private String projectApi;

	@Bean
	public MultipartResolver multipartResolver() {
		return new StandardServletMultipartResolver();
	}

	public String getKafkaHost() {
		return this.kafkaHost;
	}

	public String getAlarmApi() {
		return this.alarmApi;
	}

	public String getProjectApi() {
		return this.projectApi;
	}
}
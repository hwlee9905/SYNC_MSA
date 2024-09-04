package project.service.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig{
	@Value("${spring.kafka.bootstrap-servers}")
	private String kafkaHost;
	
	@Value("${files.upload-dir.project.title}")
	private String imgStoragePath;

	public String getKafkaHost() {
		return this.kafkaHost;
	}
	
	public String getImgStoragePath() {
		return this.imgStoragePath;
	}
}

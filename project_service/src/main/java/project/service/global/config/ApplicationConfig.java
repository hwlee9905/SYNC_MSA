package project.service.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfig{
	@Value("${spring.kafka.bootstrap-servers}")
	private String kafkaHost;
	
	@Value("${files.upload-dir.project.title}")
	private String projectThumbnailStoragePath;
	
	@Value("${files.upload-dir.task.title}")
	private String taskThumbnailStoragePath;

	public String getKafkaHost() {
		return this.kafkaHost;
	}
	
	public String getProjectThumbnailStoragePath() {
		return this.projectThumbnailStoragePath;
	}
	
	public String getTaskThumbnailStoragepath() {
		return this.taskThumbnailStoragePath;
	}
}

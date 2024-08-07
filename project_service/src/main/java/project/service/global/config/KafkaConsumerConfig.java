package project.service.global.config;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import project.service.kafka.event.*;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConsumerConfig {
	private final ApplicationConfig applicationConfig;
	private Map<String, Object> commonConsumerProps(String valueType) {
		Map<String, Object> consumerProps = new HashMap<>();
		consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, applicationConfig.getKafkaHost());
		consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class);
		consumerProps.put(ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class.getName());
		consumerProps.put(ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, JsonDeserializer.class.getName());
		consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
		consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, valueType);
		return consumerProps;
	}
	private KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> createFactory(String valueType) {
		Map<String, Object> consumerProps = commonConsumerProps(valueType);
		ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory);
		factory.getContainerProperties().setGroupId("console-consumer-" + System.currentTimeMillis());
		factory.setConcurrency(3);
		return factory;
	}
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaProjectCreateEventListenerContainerFactory() {
		return createFactory(ProjectCreateEvent.class.getName());
	}
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaTaskCreateEventListenerContainerFactory() {
		return createFactory(TaskCreateEvent.class.getName());
	}
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaAddUserToTaskEventListenerContainerFactory() {
		return createFactory(UserAddToTaskEvent.class.getName());
	}
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaDeleteTaskEventListenerContainerFactory() {
		return createFactory(TaskDeleteEvent.class.getName());
	}
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaProjectDeleteEventListenerContainerFactory() {
		return createFactory(ProjectDeleteEvent.class.getName());
	}
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaProjectUpdateEventListenerContainerFactory() {
		return createFactory(ProjectUpdateEvent.class.getName());
	}
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaTaskUpdateEventListenerContainerFactory() {
		return createFactory(TaskUpdateEvent.class.getName());
	}
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaIsExistProjectByMemberAddToProjectEventListenerContainerFactory() {
		return createFactory(IsExistProjectByMemberAddToProjectEvent.class.getName());
	}
	@Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, applicationConfig.getKafkaHost());
        return new KafkaAdmin(configs);
    }
}
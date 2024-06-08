package com.simple.book.domain.alarm.service;

import java.util.Map;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import com.simple.book.domain.alarm.dto.TopicDto;
import com.simple.book.domain.alarm.dto.req.ReqTopicDto;
import com.simple.book.domain.alarm.repository.TopicRepository;

@Service
public class AlarmService {
	private final KafkaTemplate<String, String> kafkaTemplate;
	private final KafkaAdmin kafkaAdmin;

	@Autowired
	private SimpMessagingTemplate messagingTemplate;

	@Autowired
	private KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory;

	@Autowired
	private TopicRepository topicRepository;

	public AlarmService(KafkaTemplate<String, String> kafkaTemplate, KafkaAdmin kafkaAdmin) {
		this.kafkaTemplate = kafkaTemplate;
		this.kafkaAdmin = kafkaAdmin;
	}

	public void sendMessage(String topic, String message) {
		kafkaTemplate.send(topic, message);
	}

	@KafkaListener(topics = "test", containerFactory = "kafkaListenerContainerFactory")
	public void listen(String message) {
		System.out.println("message: " + message);
		messagingTemplate.convertAndSend("/topic/messages", message);
	}

	// Test Listener
	public void addListener() {
		
//		ContainerProperties containerProps = new ContainerProperties(topic);
//		containerProps.setMessageListener((MessageListener<String, String>) record -> {
//			// 메시지 처리 로직
//			System.out.println("Received message: " + record.value());
//		});
//
//		KafkaMessageListenerContainer<String, String> container = new KafkaMessageListenerContainer<>(
//				kafkaListenerContainerFactory.getConsumerFactory(), containerProps);
//		container.start();
	}

	public void createTopic(ReqTopicDto body) {
		NewTopic newTopic = new NewTopic(body.getName(), 1, (short) 1);
		kafkaAdmin.createOrModifyTopics(newTopic);

		topicRepository.save(setDto(body).toEntity());
	}

	public void deleteTopic(ReqTopicDto body) {
		String topicname = body.getName();
//    	try (AdminClient adminClient = createAdminClient()) {
//            DeleteTopicsResult deleteTopicsResult = adminClient.deleteTopics(Collections.singletonList(topicname));
//            KafkaFuture<Void> future = deleteTopicsResult.all();
//            future.get();
//        } catch (Exception e) {
//        	e.printStackTrace();
//        }

		if (topicRepository.deleteByName(topicname) == 0) {
			System.out.println("성공이다");
		} else {
			System.out.println("실패다");
		}
	}

	private TopicDto setDto(ReqTopicDto body) {
		TopicDto dto = new TopicDto();
		dto.setName(body.getName());
		dto.setType(body.getType());
		dto.setId(body.getId());
		return dto;
	}

	private AdminClient createAdminClient() {
		Map<String, Object> config = kafkaAdmin.getConfigurationProperties();
		return AdminClient.create(config);
	}

}
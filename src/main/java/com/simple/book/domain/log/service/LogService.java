package com.simple.book.domain.log.service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class LogService {
	private final KafkaTemplate<String, String> kafkaTemplate;
	
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
	
	@Autowired
    private KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory;
	
    private long lastReadPosition = 0;
    
    @Autowired
	public LogService(KafkaTemplate<String, String> kafkaTemplate) {
		this.kafkaTemplate=kafkaTemplate;
	}
    
//    @PostConstruct
//    public void initialize() {
//        readLog();
//    }
    
	public void readLog() {
		Path logDir = Paths.get("/takebook/logs");

        try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
            logDir.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY);

            while (true) {
                WatchKey key;
                try {
                    key = watchService.take();
                } catch (InterruptedException ex) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();

//                    Path fileName = (Path) event.context();
//                    Path fullPath = logDir.resolve(fileName);

                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                    	try (RandomAccessFile file = new RandomAccessFile(new File(logDir.toString() + File.separator + "takebook.log"), "r")) {
                            // 파일의 변경된 부분으로 이동
                            file.seek(lastReadPosition);

                            String line;
                            while ((line = file.readLine()) != null) {
                                // 변경된 부분에 대한 처리 (여기서는 간단히 콘솔에 출력)
                                kafkaTemplate.send("log", line);
                            }

                            // 파일의 현재 위치를 기록하여 다음 읽기 시 이전 위치부터 읽도록 함
                            lastReadPosition = file.getFilePointer();
                        }catch (Exception e) {
                        	e.printStackTrace();
						}
//                        System.out.println("파일 수정됨: " + fullPath);
//
//                        try {
//                            List<String> lines = Files.readAllLines(fullPath);
//                            for (String line : lines) {
//                                System.out.println(line);
//                                kafkaTemplate.send("log", line);
//                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                    }
                }

                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
//	public void test() {
//		Properties props = new Properties();
//        props.put("bootstrap.servers", "localhost:9092"); // Kafka bootstrap 서버 설정
//        try (AdminClient adminClient = KafkaAdminClient.create(props)) {
//            // Consumer Group 목록 조회
//            for (ConsumerGroupListing groupListing : adminClient.listConsumerGroups().all().get()) {
//                String groupId = groupListing.groupId();
//                // Consumer Group의 정보 조회
//                ConsumerGroupDescription groupDescription = adminClient.describeConsumerGroups(
//                        java.util.Collections.singleton(groupId)).all().get().get(groupId);
//                // Consumer Group이 소비하는 토픽과 파티션 별 Consumer Lag 조회
//                Map<TopicPartition, OffsetAndMetadata> lag = adminClient.listConsumerGroupOffsets(groupId).partitionsToOffsetAndMetadata().get();
//                for (Map.Entry<TopicPartition, OffsetAndMetadata> entry : lag.entrySet()) {
//                    TopicPartition partition = entry.getKey();
//                    OffsetAndMetadata offsetAndMetadata = entry.getValue();
//                    long latestOffset = adminClient.listOffsets(java.util.Collections.singletonMap(partition, adminClient.describeTopics(
//                            java.util.Collections.singleton(partition.topic())).values().get(partition.topic()).get().partitions().get(partition.partition()).leader().offset())).partitionsToOffset().get(partition).offset();
//                    long consumerLag = latestOffset - offsetAndMetadata.offset();
//                    System.out.println("Consumer Group: " + groupId + ", Topic: " + partition.topic() + ", Partition: " + partition.partition() + ", Lag: " + consumerLag);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//	}
	
	
	@KafkaListener(topics = "log", containerFactory = "kafkaListenerContainerFactory")
    public void listen(String message) {
    	messagingTemplate.convertAndSend("/topic/messages", message);
    }
}

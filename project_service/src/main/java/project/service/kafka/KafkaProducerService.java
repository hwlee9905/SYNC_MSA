package project.service.kafka;
import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.service.kafka.event.RollbackMemberAddToProjectEvent;
import project.service.kafka.event.UserAddToProjectEvent;
import project.service.kafka.event.UserAddToProjectLinkEvent;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "member-add-to-project-topic";
    private static final String TOPIC1 = "link-add-to-project-topic";
    private static final String ROLL_BACK_TOPIC = "rollback-member-add-to-project-topic";

    public void sendAddMemberToProjectEvent(String userId, Long projectId) {
        UserAddToProjectEvent event = new UserAddToProjectEvent(projectId, userId);
        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC, event);
        record.headers().remove("spring.json.header.types");
        kafkaTemplate.send(record);
    }
    
    public void sendAddLinkToProjectEvent(Long projectId) {
    	UserAddToProjectLinkEvent event = new UserAddToProjectLinkEvent(projectId);
    	ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC1, event);
    	record.headers().remove("spring.json.header.types");
    	kafkaTemplate.send(record);
    }

    /**
     * RollBack
     * @param projectId
     * @param userIds
     */
    public void sendRollbackMemberAddToProjectEvent(Long projectId, List<String> userIds) {
        RollbackMemberAddToProjectEvent event = new RollbackMemberAddToProjectEvent(projectId, userIds);
        ProducerRecord<String, Object> record = new ProducerRecord<>(ROLL_BACK_TOPIC, event);
        record.headers().remove("spring.json.header.types");
        kafkaTemplate.send(record);
    }
    
}

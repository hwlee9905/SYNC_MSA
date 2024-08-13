package user.service.kafka.member;

import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import user.service.global.advice.SuccessResponse;
import user.service.kafka.member.event.IsExistProjectByMemberAddToProjectEvent;

@Service
@RequiredArgsConstructor
public class KafkaMemberProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "is-exist-project-by-member-add-to-project-topic";
    /**
     * 프로젝트 존재 여부 확인 이벤트 생성
     * @param projectId, userIds
     * @return
     */
//    public SuccessResponse isExistProjectByMemberAddToProject(Long projectId, List<String> userIds) {
//        IsExistProjectByMemberAddToProjectEvent event = new IsExistProjectByMemberAddToProjectEvent(projectId, userIds);
//        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC, event);
//        record.headers().remove("spring.json.header.types");
//        kafkaTemplate.send(record);
//        return new SuccessResponse("업무 생성 이벤트 생성", event);
//    }
    // New
    // 작성자 : 강민경
    public SuccessResponse isExistProjectByMemberAddToProject(Long projectId, List<String> userIds) {
        IsExistProjectByMemberAddToProjectEvent event = new IsExistProjectByMemberAddToProjectEvent(projectId, userIds);
        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC, event);
        record.headers().remove("spring.json.header.types");
        kafkaTemplate.send(record);
        return SuccessResponse.builder().message("업무 생성 이벤트 생성").data(event).build();
    }
}

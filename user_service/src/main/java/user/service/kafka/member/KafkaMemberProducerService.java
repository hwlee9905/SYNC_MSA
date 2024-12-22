package user.service.kafka.member;

import java.util.List;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import user.service.UserService;
import user.service.global.advice.SuccessResponse;
import user.service.kafka.member.event.DeleteMemberFromTaskEvent;
import user.service.kafka.member.event.IsExistProjectByMemberAddToProjectEvent;
import user.service.web.dto.member.request.MemberRemoveRequestDto;

@Service
@RequiredArgsConstructor
public class KafkaMemberProducerService {
    private final UserService userService;
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private static final String TOPIC = "is-exist-project-by-member-add-to-project-topic";
    private static final String TOPIC1 = "task-remove-user-topic";

    /**
     * 프로젝트 존재 여부 확인 이벤트 생성
     * @param projectId, userIds
     * @return
     */
    // New
    // 작성자 : 강민경
    public SuccessResponse isExistProjectByMemberAddToProject(Long projectId, List<String> userIds) {
        IsExistProjectByMemberAddToProjectEvent event = new IsExistProjectByMemberAddToProjectEvent(projectId, userIds);
        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC, event);
        record.headers().remove("spring.json.header.types");
        kafkaTemplate.send(record);
        return SuccessResponse.builder().message("업무 생성 이벤트 생성").data(event).build();
    }
    public SuccessResponse sendRemoveUserFromTaskEvent(MemberRemoveRequestDto memberRemoveRequestDto) {
        Long userId = userService.getUserEntityId(memberRemoveRequestDto.getUserId());
        DeleteMemberFromTaskEvent event = new DeleteMemberFromTaskEvent(memberRemoveRequestDto.getTaskId(), userId);
        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC1, event);
        record.headers().remove("spring.json.header.types");
        kafkaTemplate.send(record);
        return SuccessResponse.builder().message("업무 담당자 삭제 이벤트 생성").data(event).build();
    }
}

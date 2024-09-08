package user.service.kafka.task;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import user.service.MemberService;
import user.service.UserService;
import user.service.entity.User;
import user.service.global.advice.SuccessResponse;
import user.service.kafka.task.event.TaskCreateEvent;
import user.service.kafka.task.event.TaskDeleteEvent;
import user.service.kafka.task.event.TaskUpdateEvent;
import user.service.kafka.task.event.UserAddToTaskEvent;
import user.service.web.dto.member.request.MemberMappingToTaskRequestDto;
import user.service.web.dto.task.request.CreateTaskRequestDto;
import user.service.web.dto.task.request.DeleteTaskRequestDto;
import user.service.web.dto.task.request.UpdateTaskRequestDto;

@Service
@RequiredArgsConstructor
public class KafkaTaskProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final UserService userService;
    private final MemberService memberService;
    private static final String TOPIC = "task-create-topic";
    private static final String TOPIC1 = "task-add-user-topic";
    private static final String TOPIC2 = "task-delete-topic";
    private static final String TOPIC3 = "task-update-topic";
    
    /**
     * 업무 생성 이벤트 생성
     * @param createTaskRequestDto
     * @return
     */
    public SuccessResponse sendCreateTaskEvent(CreateTaskRequestDto createTaskRequestDto, List<MultipartFile> descriptionFiles) throws IOException {
        User user = userService.findUserEntity(userService.getCurrentUserId());
        memberService.findMemberByUserIdAndProjectId(user.getId(), createTaskRequestDto.getProjectId());

        List<TaskCreateEvent.FileData> fileDataList = descriptionFiles != null ?
            descriptionFiles.stream()
                .map(file -> {
                    try {
                        return new TaskCreateEvent.FileData(file.getOriginalFilename(), file.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to convert file", e);
                    }
                })
                .collect(Collectors.toList()) :
            Collections.emptyList();

        TaskCreateEvent event = new TaskCreateEvent(createTaskRequestDto, fileDataList);
        kafkaTemplate.send(TOPIC, event);
        return SuccessResponse.builder().message("업무 생성 이벤트 생성").data(createTaskRequestDto).build();
    }
    
    /**
     * 업무 담당자 배정 이벤트 생성
     * @param memberMappingToTaskRequestDto
     * @return
     */
    public SuccessResponse sendAddUserToTaskEvent(MemberMappingToTaskRequestDto memberMappingToTaskRequestDto) {
        SuccessResponse responseMessage = memberService.allMembersInSameProject(memberMappingToTaskRequestDto);
        if(responseMessage.isResult()){
            @SuppressWarnings("unchecked")
            List<Long> userIds = (List<Long>) responseMessage.getData();
            UserAddToTaskEvent event = new UserAddToTaskEvent(userIds, memberMappingToTaskRequestDto.getTaskId());
            ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC1, event);
            record.headers().remove("spring.json.header.types");
            kafkaTemplate.send(record);
            return SuccessResponse.builder().message("업무 담당자 배정 이벤트 생성").data(memberMappingToTaskRequestDto).build();
        } else{
            return SuccessResponse.builder().message(responseMessage.getMessage()).data(responseMessage.getData()).build();
        }
    }
    
    public SuccessResponse sendDeleteTaskEvent(DeleteTaskRequestDto deleteTaskRequestDto) {
        User user = userService.findUserEntity(userService.getCurrentUserId());
        // 프로젝트의 멤버인지 확인
        memberService.findMemberByUserIdAndProjectId(user.getId(), deleteTaskRequestDto.getProjectId());
        TaskDeleteEvent event = new TaskDeleteEvent(deleteTaskRequestDto.getTaskId());
        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC2, event);
        record.headers().remove("spring.json.header.types");
        kafkaTemplate.send(record);
        return SuccessResponse.builder().message("업무 삭제 이벤트 생성").data(deleteTaskRequestDto).build();
    }

    public SuccessResponse sendUpdateTaskEvent(UpdateTaskRequestDto updateTaskRequestDto, List<MultipartFile> descriptionFiles) throws IOException {
        User user = userService.findUserEntity(userService.getCurrentUserId());
        memberService.findMemberByUserIdAndProjectId(user.getId(), updateTaskRequestDto.getProjectId());

        List<TaskUpdateEvent.FileData> fileDataList = descriptionFiles != null ?
                descriptionFiles.stream()
                        .map(file -> {
                            try {
                                return new TaskUpdateEvent.FileData(file.getOriginalFilename(), file.getBytes());
                            } catch (IOException e) {
                                throw new RuntimeException("Failed to convert file", e);
                            }
                        })
                        .collect(Collectors.toList()) :
                Collections.emptyList();

        TaskUpdateEvent event = new TaskUpdateEvent(updateTaskRequestDto, fileDataList);
        kafkaTemplate.send(TOPIC3, event);
        return SuccessResponse.builder().message("업무 수정 이벤트 생성").data(updateTaskRequestDto).build();
    }
}

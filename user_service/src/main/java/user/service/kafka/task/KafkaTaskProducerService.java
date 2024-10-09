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
import user.service.global.exception.ImageConversionFailedException;
import user.service.global.util.ExtsnFilter;
import user.service.kafka.member.event.DeleteMemberFromTaskEvent;
import user.service.kafka.task.event.TaskCreateEvent;
import user.service.kafka.task.event.TaskDeleteEvent;
import user.service.kafka.task.event.TaskUpdateEvent;
import user.service.kafka.task.event.UserAddToTaskEvent;
import user.service.web.dto.member.request.MemberMappingToTaskRequestDto;
import user.service.web.dto.member.request.MemberRemoveRequestDto;
import user.service.web.dto.task.request.CreateTaskRequestDto;
import user.service.web.dto.task.request.DeleteTaskRequestDto;
import user.service.web.dto.task.request.UpdateTaskRequestDto;

@Service
@RequiredArgsConstructor
public class KafkaTaskProducerService {
    private final KafkaTemplate<String, Object> kafkaTemplate;
    private final UserService userService;
    private final MemberService memberService;
    private final ExtsnFilter extsnFilter;
    
    private static final String TOPIC = "task-create-topic";
    private static final String TOPIC1 = "task-add-user-topic";
    private static final String TOPIC2 = "task-delete-topic";
    private static final String TOPIC3 = "task-update-topic";
    private static final String TOPIC4 = "task-remove-user-topic";
    /**
     * 업무 생성 이벤트 생성
     * @param createTaskRequestDto
     * @return
     */
    public SuccessResponse sendCreateTaskEvent(CreateTaskRequestDto createTaskRequestDto, List<MultipartFile> descriptionFiles, MultipartFile thumbnailImage) throws IOException {
        User user = userService.findUserEntity(userService.getCurrentUserId());
        memberService.findMemberByUserIdAndProjectId(user.getId(), createTaskRequestDto.getProjectId());

        TaskCreateEvent event;
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

        // 이모지, 아이콘 둘다 존재할 경우 예외 처리
        if (createTaskRequestDto.getThumbnailIcon() != null && thumbnailImage != null) {
            throw new IllegalArgumentException("이모지와 아이콘 둘 다 존재할 수 없습니다.");
        } else if (createTaskRequestDto.getThumbnailIcon() != null) {
            event = new TaskCreateEvent(createTaskRequestDto, fileDataList, null, null);
        } else if (thumbnailImage != null) {
            byte[] imgByte = null;
            try {
                imgByte = thumbnailImage.getBytes();
            } catch (IOException e) {
                throw new ImageConversionFailedException(e.getMessage());
            }
            event = new TaskCreateEvent(createTaskRequestDto, fileDataList, imgByte, extsnFilter.getExtension(thumbnailImage));
        } else {
            event = new TaskCreateEvent(createTaskRequestDto, fileDataList, null, null);
        }

        kafkaTemplate.send(TOPIC, event);
        return SuccessResponse.builder().message("업무 생성 이벤트 생성").data(createTaskRequestDto).build();
    }
    
    /**
     * 업무 담당자 배정 이벤트 생성
     * @param memberMappingToTaskRequestDto
     * @return
     */
    public SuccessResponse sendAddUserToTaskEvent(MemberMappingToTaskRequestDto memberMappingToTaskRequestDto) {
        Boolean inSameProject = memberService.allMembersInSameProject(memberMappingToTaskRequestDto);
        if(inSameProject){
            List<Long> userIds = memberMappingToTaskRequestDto.getUserIds();
            UserAddToTaskEvent event = new UserAddToTaskEvent(userIds, memberMappingToTaskRequestDto.getTaskId());
            ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC1, event);
            record.headers().remove("spring.json.header.types");
            kafkaTemplate.send(record);
            return SuccessResponse.builder().message("업무 담당자 배정 이벤트 생성").data(memberMappingToTaskRequestDto).build();
        } else{
            return SuccessResponse.builder().message("모든 유저들이 같은 프로젝트에 속해있지 않습니다.").data("").build();
        }
    }
    public SuccessResponse sendRemoveUserFromTaskEvent(MemberRemoveRequestDto memberRemoveRequestDto) {
        Long userId = userService.getUserEntityId(memberRemoveRequestDto.getUserId());
        DeleteMemberFromTaskEvent event = new DeleteMemberFromTaskEvent(userId, memberRemoveRequestDto.getTaskId());
        ProducerRecord<String, Object> record = new ProducerRecord<>(TOPIC4, event);
        record.headers().remove("spring.json.header.types");
        kafkaTemplate.send(record);
        return SuccessResponse.builder().message("업무 담당자 삭제 이벤트 생성").data(event).build();
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

    public SuccessResponse sendUpdateTaskEvent(UpdateTaskRequestDto updateTaskRequestDto, List<MultipartFile> descriptionFiles, List<MultipartFile> deletedImages) throws IOException {
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
        List<TaskUpdateEvent.FileData> deletedFileDataList = deletedImages != null ?
            deletedImages.stream()
                .map(file -> {
                    try {
                        return new TaskUpdateEvent.FileData(file.getOriginalFilename(), file.getBytes());
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to convert file", e);
                    }
                })
                .collect(Collectors.toList()) :
            Collections.emptyList();
        TaskUpdateEvent event = new TaskUpdateEvent(updateTaskRequestDto, fileDataList, deletedFileDataList);
        kafkaTemplate.send(TOPIC3, event);
        return SuccessResponse.builder().message("업무 수정 이벤트 생성").data(updateTaskRequestDto).build();
    }

}

package project.service.kafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.service.ProjectService;
import project.service.TaskService;
import project.service.dto.request.CreateProjectRequestDto;
import project.service.dto.request.CreateTaskRequestDto;
import project.service.entity.Project;
import project.service.global.SuccessResponse;
import project.service.kafka.event.*;


@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaConsumerService {
    private final ProjectService projectService;
    private final TaskService taskService;
    private final KafkaProducerService kafkaProducerService;
    
    // Project
    private static final String TOPIC = "project-create-topic";
    private static final String TOPIC4 = "project-delete-topic";
    private static final String TOPIC5 = "project-update-topic";
    private static final String TOPIC7 = "is-exist-project-by-member-add-to-project-topic";
    private static final String TOPIC8 = "project-add-img-topic";
    private static final String TOPIC9 = "project-add-icon-topic";
    private static final String TOPIC10 = "task-remove-user-topic";
    
    // Task
    private static final String TOPIC1 = "task-create-topic";
    private static final String TOPIC2 = "task-add-user-topic";
    private static final String TOPIC3 = "task-delete-topic";
    private static final String TOPIC6 = "task-update-topic";
    
    @KafkaListener(topics = TOPIC, groupId = "project_create_group", containerFactory = "kafkaProjectCreateEventListenerContainerFactory")
    public void listenProjectCreateEvent(ProjectCreateEvent event) {
        try {
            CreateProjectRequestDto projectCreateRequestDto = event.getProjectCreateRequestDto();
            String userId = event.getUserId();
            // 이벤트 처리
            Project project = projectService.createProject(projectCreateRequestDto, event.getImg(), event.getExtsn());
            log.warn("project.getId() : " + project.getId());
            kafkaProducerService.sendAddMemberToProjectEvent(userId ,project.getId());
            // 초대 링크 생성
            kafkaProducerService.sendAddLinkToProjectEvent(project.getId());
            // 처리 로그 출력
            log.info("Processed ProjectCreateEvent for userId: " + userId);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    
    @KafkaListener(topics = TOPIC1, groupId = "task-create-group", containerFactory = "kafkaTaskCreateEventListenerContainerFactory")
    public void listenTaskCreateEvent(TaskCreateEvent event) {
        try {
            CreateTaskRequestDto createTaskRequestDto = event.getCreateTaskRequestDto();
            // 이벤트 처리
            taskService.createTask(createTaskRequestDto, event.getFiles(), event.getThumbnailByte(), event.getExtsn());
            // 처리 로그 출력
            log.info("Processed TaskCreateEvent");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    @KafkaListener(topics = TOPIC2, groupId = "task-add-user-group", containerFactory = "kafkaAddUserToTaskEventListenerContainerFactory")
    public void listenAddUserToTaskEvent(UserAddToTaskEvent event) {
        try {
            // 이벤트 처리
            taskService.addUserToTask(event);
            // 처리 로그 출력
            log.info("Processed addUserToTaskEvent");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    @KafkaListener(topics = TOPIC3, groupId = "task-delete-group", containerFactory = "kafkaDeleteTaskEventListenerContainerFactory")
    public void listenDeleteTaskEvent(TaskDeleteEvent event) {
        try {
            // 이벤트 처리
            taskService.deleteTask(event);
            // 처리 로그 출력
            log.info("Processed addUserToTaskEvent");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    @KafkaListener(topics = TOPIC4, groupId = "project-delete-group", containerFactory = "kafkaProjectDeleteEventListenerContainerFactory")
    public void listenProjectDeleteEvent(ProjectDeleteEvent event) {
        try {
            // 이벤트 처리
            projectService.deleteProject(event);
            // 처리 로그 출력
            log.info("Processed projectDeleteEvent");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    @KafkaListener(topics = TOPIC5, groupId = "project-update-group", containerFactory = "kafkaProjectUpdateEventListenerContainerFactory")
    public void listenProjectUpdateEventEvent(ProjectUpdateEvent event) {
        try {
            // 이벤트 처리
            projectService.updateProject(event);
            // 처리 로그 출력
            log.info("Processed projectUpdateEvent");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    @KafkaListener(topics = TOPIC6, groupId = "task-update-group", containerFactory = "kafkaTaskUpdateEventListenerContainerFactory")
    public void listenTaskUpdateEventEvent(TaskUpdateEvent event) {
        try {
            // 이벤트 처리
            taskService.updateTask(event);
            // 처리 로그 출력
            log.info("Processed taskUpdateEvent");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    @KafkaListener(topics = TOPIC7, groupId = "is-exist-project-by-member-add-to-project-group", containerFactory = "kafkaIsExistProjectByMemberAddToProjectEventListenerContainerFactory")
    public void listenIsExistProjectByMemberAddToProjectEventEvent(IsExistProjectByMemberAddToProjectEvent event) {
        try {
            // 이벤트 처리
        	SuccessResponse responseMessage = projectService.findProject(event.getProjectId());
            if (!responseMessage.isResult()) {
                kafkaProducerService.sendRollbackMemberAddToProjectEvent(event.getProjectId(), event.getUserIds());
            }
            // 처리 로그 출력
            log.info("Processed IsExistProjectByMemberAddToProjectEvent");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
    
    @KafkaListener(topics = TOPIC8, groupId = "project_create_group", containerFactory = "kafkaProjectAddImgEventListenerContainerFactory")
    public void listenProjectAddImgEvent(ProjectAddImgEvent event) {
    	System.out.println("img: " + event);
    }
    
    @KafkaListener(topics = TOPIC9, groupId = "project_create_group", containerFactory = "kafkaProjectAddIconEventListenerContainerFactory")
    public void listenProjectAddIconEvent(ProjectAddIconEvent event) {
    	System.out.println("icon: " + event);
    }
    @KafkaListener(topics = TOPIC10, groupId = "task-remove-user-group", containerFactory = "kafkaDeleteFromMemberFromTaskEventListenerContainerFactory")
    public void listenDeleteFromMemberFromTaskEvent(DeleteMemberFromTaskEvent event) {
        try {
            taskService.removeUserFromTask(event);
            log.info("Processed DeleteFromMemberFromTaskEvent");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

}
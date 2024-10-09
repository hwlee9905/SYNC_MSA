package project.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.service.dto.request.CreateTaskRequestDto;
import project.service.dto.request.MemberRemoveRequestDto;
import project.service.dto.response.*;
import project.service.dto.request.UpdateTaskRequestDto;
import project.service.dto.response.GetMemberFromTaskResponseDto;
import project.service.dto.response.GetTaskResponseDto;
import project.service.dto.response.GetTasksByProjectIdResponseDto;
import project.service.dto.response.GetTasksResponseDto;
import project.service.entity.Project;
import project.service.entity.Task;
import project.service.entity.TaskImage;
import project.service.entity.UserTask;
import project.service.entity.UserTaskId;
import project.service.global.SuccessResponse;
import project.service.global.util.FileManagement;
import project.service.kafka.event.*;
import project.service.repository.ProjectRepository;
import project.service.repository.TaskImageRepository;
import project.service.repository.TaskRepository;
import project.service.repository.UserTaskRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserTaskRepository userTaskRepository;
    private final FileStorageService fileStorageService;
    private final TaskImageRepository taskImageRepository;
    private final FileManagement fileManagement;
    
    @Transactional(rollbackFor = { Exception.class })
    public ResponseEntity<Resource> getImage(String filename) {
        try {
            // 파일 경로에서 특수 문자 제거
            String cleanedFilename = filename.replaceAll("[^\\x20-\\x7E]", "");
            Path filePath = Paths.get(cleanedFilename).normalize();
            log.info("getImage: filePath={}", filePath);
            if (!Files.exists(filePath)) {
                throw new IOException("File not found: " + cleanedFilename);
            }
            Resource resource = new UrlResource(filePath.toUri());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
    @Transactional(rollbackFor = { Exception.class })
    public SuccessResponse getTask(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));

        // TaskImage 엔티티를 조회하여 이미지 목록을 가져옴
        List<TaskImage> taskImages = taskImageRepository.findByTaskId(taskId);
        List<File> imageFiles = taskImages.stream()
            .map(taskImage -> {
                String filePath = taskImage.getImagePath();
                File file = new File(filePath);
                if (file.exists()) {
                    return file;
                } else {
                    log.warn("File not found: " + filePath);
                    return null;
                }
            })
            .filter(Objects::nonNull) // null이 아닌 것만 필터링
            .collect(Collectors.toList());

        //path와 filename을 분리하여 response 할 것
        // GetTaskResponseDto 객체 생성
        GetTaskResponseDto result = GetTaskResponseDto.fromEntity(task, imageFiles);

        return SuccessResponse.builder().data(result).build();
    }
    private String removeUUIDFromFileName(String filePath) {
        String fileName = Paths.get(filePath).getFileName().toString();
        int underscoreIndex = fileName.indexOf("_");
        return fileName.substring(underscoreIndex + 1);
    }

    @Transactional(rollbackFor = { Exception.class })
    public SuccessResponse getOnlyChildrenTasks(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));
        GetTasksResponseDto result = GetTasksResponseDto.fromEntityOnlyChildrenTasks(task);
        return SuccessResponse.builder().data(result).build();
    }
    @Transactional(rollbackFor = { Exception.class })
    public void addUserToTask(UserAddToTaskEvent userAddToTaskEvent) {
        Optional<Task> task = taskRepository.findById(userAddToTaskEvent.getTaskId());
        //task id 존재하지 않는경우 예외처리 해야함 (추가)
        List<Long> userIds = userAddToTaskEvent.getUserIds();
        userIds.stream().forEach(userId -> {
            UserTaskId userTaskId = UserTaskId.builder()
                .userId(userId)
                .taskId(task.get().getId())
                .build();
            UserTask userTask = UserTask.builder()
                .task(task.get())
                .id(userTaskId).build();
            userTaskRepository.save(userTask);
        });
    }

    @Transactional(rollbackFor = { Exception.class })
    public void deleteTask(TaskDeleteEvent event) {
        Optional<Task> task = taskRepository.findById(event.getTaskId());
        if (!task.isPresent()) {
            throw new EntityNotFoundException("Task not found with ID: " + event.getTaskId());
        }
        Task taskEntity = task.get();
        if (taskEntity.getParentTask() == null) {
            Project project = taskEntity.getProject();
            project.setChildCount(project.getChildCount() - 1);
            project.setChildCompleteCount(project.getChildCompleteCount() - 1);
            projectRepository.save(project);
        } else {
            Task parentTask = taskEntity.getParentTask();
            parentTask.setChildCount(parentTask.getChildCount() - 1);
            parentTask.setChildCompleteCount(parentTask.getChildCompleteCount() - 1);
            taskRepository.save(parentTask);
        }
        taskRepository.delete(task.get());
    }
    @Transactional(rollbackFor = { Exception.class })
    public void updateTask(TaskUpdateEvent event) {
        UpdateTaskRequestDto updateTaskRequestDto = event.getUpdateTaskRequestDto();
        Optional<Task> task = taskRepository.findById(updateTaskRequestDto.getTaskId());
        if (!task.isPresent()) {
            throw new EntityNotFoundException("Task not found with ID: " + updateTaskRequestDto.getTaskId());
        }

        Task taskEntity = task.get();
        int oldStatus = taskEntity.getStatus();
        int newStatus = updateTaskRequestDto.getStatus();

        taskEntity.setTitle(updateTaskRequestDto.getTitle());
        taskEntity.setDescription(updateTaskRequestDto.getDescription());
        taskEntity.setStartDate(updateTaskRequestDto.getStartDate());
        taskEntity.setEndDate(updateTaskRequestDto.getEndDate());
        taskEntity.setStatus(newStatus);

        Project project = taskEntity.getProject();
        if (updateTaskRequestDto.getStartDate().before(project.getStartDate())) {
            project.setStartDate(updateTaskRequestDto.getStartDate());
        }

        if (updateTaskRequestDto.getEndDate().after(project.getEndDate())) {
            project.setEndDate(updateTaskRequestDto.getEndDate());
        }

        projectRepository.save(project);

        if (taskEntity.getParentTask() != null) {
            Task parentTask = taskEntity.getParentTask();
            updateChildCompleteCount(parentTask, oldStatus, newStatus);
            taskRepository.save(parentTask);
        } else {
            updateChildCompleteCountForProject(project, oldStatus, newStatus);
            projectRepository.save(project);
        }

        taskRepository.save(taskEntity);

        // 삭제할 파일 처리
        try {
            fileStorageService.deleteFiles(event.getDeletedImages());
        } catch (IOException e) {
            log.error("Failed to delete files", e);
            throw new RuntimeException("Failed to delete files", e);
        }

        // 저장할 파일 처리
        try {
            fileStorageService.saveFiles(taskEntity, event.getDescriptionFiles());
        } catch (IOException e) {
            log.error("Failed to save files", e);
            throw new RuntimeException("Failed to save files", e);
        }
    }
    private void updateChildCompleteCountForProject(Project project, int oldStatus, int newStatus) {
        Optional.of(project)
                .filter(proj -> oldStatus != 2 && newStatus == 2)
                .ifPresent(proj -> proj.setChildCompleteCount(proj.getChildCompleteCount() + 1));

        Optional.of(project)
                .filter(proj -> oldStatus == 2 && newStatus != 2)
                .ifPresent(proj -> proj.setChildCompleteCount(proj.getChildCompleteCount() - 1));
    }
    @Transactional(rollbackFor = { Exception.class })
    public void createTask(CreateTaskRequestDto createTaskRequestDto, List<TaskCreateEvent.FileData> files, byte[] thumbnailByte, String extsn) throws IOException {
        Project project = projectRepository.findById(createTaskRequestDto.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + createTaskRequestDto.getProjectId()));
        Optional<Task> parentTask = taskRepository.findById(createTaskRequestDto.getParentTaskId());

        Task task = new Task();
        task.setTitle(createTaskRequestDto.getTitle());
        task.setChildCompleteCount(0);
        task.setChildCount(0);
        task.setDescription(createTaskRequestDto.getDescription());
        task.setStartDate(createTaskRequestDto.getStartDate());
        task.setEndDate(createTaskRequestDto.getEndDate());
        task.setStatus(createTaskRequestDto.getStatus());
        task.setProject(project);

        if (createTaskRequestDto.getStartDate().before(project.getStartDate())) {
            project.setStartDate(createTaskRequestDto.getStartDate());
        }

        if (createTaskRequestDto.getEndDate().after(project.getEndDate())) {
            project.setEndDate(createTaskRequestDto.getEndDate());
        }

        projectRepository.save(project);
        if (parentTask.isPresent()) {
            if (parentTask.get().getDepth() == 2) {
                throw new IllegalArgumentException("Parent task cannot have a depth of 2.");
            }
            Task parentTaskEntity = parentTask.get();
            parentTaskEntity.setChildCount(parentTaskEntity.getChildCount() + 1);

            task.setDepth(parentTask.get().getDepth() + 1);
            task.setParentTask(parentTaskEntity);
        } else {
            project.setChildCount(project.getChildCount() + 1);

            task.setDepth(0);
        }

        String thumbnail;
        if (thumbnailByte != null && createTaskRequestDto.getThumbnailIcon() == null) {
            thumbnail = UUID.randomUUID().toString() + "." + extsn;
            // 만약 Exception 발생하면 저장된 썸네일 이미지도 삭제 시켜야함 (롤백)
            // 나중에 개발...ㅋㅋ...
            fileManagement.uploadThumbnail(thumbnailByte, thumbnail, 'T');
            task.setThumbnail(thumbnail);
            task.setThumbnailType('M');
        } else if(createTaskRequestDto.getThumbnailIcon() != null && thumbnailByte == null) {
            thumbnail = createTaskRequestDto.getThumbnailIcon();
            task.setThumbnail(thumbnail);
            task.setThumbnailType('C');
        } else {
            task.setThumbnailType('N');
        }

        taskRepository.save(task);
        if (files != null) {
            fileStorageService.saveFiles(task, files);
        }
    }
    private void updateChildCompleteCount(Task parentTask, int oldStatus, int newStatus) {
        Optional.of(parentTask)
                .filter(task -> oldStatus != 2 && newStatus == 2)
                .ifPresent(task -> task.setChildCompleteCount(task.getChildCompleteCount() + 1));

        Optional.of(parentTask)
                .filter(task -> oldStatus == 2 && newStatus != 2)
                .ifPresent(task -> task.setChildCompleteCount(task.getChildCompleteCount() - 1));
    }
    public SuccessResponse getUserIdsFromTask(Long taskId) {
        List<UserTask> userTasks = userTaskRepository.findByTaskId(taskId);
        GetMemberFromTaskResponseDto result = GetMemberFromTaskResponseDto.builder()
            .userIds(userTasks.stream()
                .map(userTask -> userTask.getId().getUserId())
                .collect(Collectors.toList()))
            .build();
        if (userTasks.isEmpty()) {
            return SuccessResponse.builder()
                .message("해당 업무에는 배정된 담당자가 없습니다.")
                .result(false)
                .build();
        }
        return SuccessResponse.builder()
            .data(result)
            .build();
    }
    @Transactional(rollbackFor = { Exception.class })
    public SuccessResponse getTaskByProjectId(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));

        List<GetTasksByProjectIdResponseDto> tasks = project.getTasks().stream()
            .filter(task -> task.getDepth() == 0)
            .map(task -> GetTasksByProjectIdResponseDto.builder()
                .taskId(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .status(task.getStatus())
                .depth(task.getDepth())
                .progress((float) project.getChildCompleteCount() / project.getChildCount())
                .build())
            .collect(Collectors.toList());

        return SuccessResponse.builder().data(tasks).build();
    }
    @Transactional(rollbackFor = { Exception.class }, propagation = Propagation.REQUIRED)
    public void removeUserFromTask(DeleteFromMemberFromTaskEvent event) {

        log.info("Starting removeUserFromTask transaction");
        UserTaskId userTaskId = UserTaskId.builder()
            .userId(event.getUserId())
            .taskId(event.getTaskId())
            .build();
        Optional<UserTask> userTask = userTaskRepository.findById(userTaskId);
        if (userTask.isEmpty()) {
            log.info("UserTask not found for userId: {}, taskId: {}", event.getUserId(), event.getTaskId());
            return;
        }
        userTaskRepository.deleteById(userTaskId);
        userTaskRepository.flush();
        log.info("Ending removeUserFromTask transaction");
    }
    @Transactional(rollbackFor = { Exception.class })
    public void removeUserFromTaskv2(Long userId, Long taskId) {
        UserTaskId userTaskId = UserTaskId.builder()
            .taskId(taskId)
            .userId(userId)
            .build();
        userTaskRepository.deleteById(userTaskId);
    }
}
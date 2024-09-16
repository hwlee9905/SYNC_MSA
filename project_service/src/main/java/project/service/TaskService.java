package project.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.service.dto.request.CreateTaskRequestDto;
import project.service.dto.response.*;
import project.service.dto.request.UpdateTaskRequestDto;
import project.service.entity.*;
import project.service.global.SuccessResponse;
import project.service.kafka.event.TaskCreateEvent;
import project.service.kafka.event.TaskDeleteEvent;
import project.service.kafka.event.TaskUpdateEvent;
import project.service.kafka.event.UserAddToTaskEvent;
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

        if (taskEntity.getParentTask() != null) {
            Task parentTask = taskEntity.getParentTask();
            updateChildCompleteCount(parentTask, oldStatus, newStatus);
            taskRepository.save(parentTask);
        } else {
            Project project = taskEntity.getProject();
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
    public void createTask(CreateTaskRequestDto createTaskRequestDto, List<TaskCreateEvent.FileData> files) throws IOException {
        Project project = projectRepository.findById(createTaskRequestDto.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + createTaskRequestDto.getProjectId()));
        Optional<Task> parentTask = taskRepository.findById(createTaskRequestDto.getParentTaskId());
        Task task;
        if (parentTask.isPresent()) {
            if (parentTask.get().getDepth() == 2) {
                throw new IllegalArgumentException("Parent task cannot have a depth of 2.");
            }
            Task parentTaskEntity = parentTask.get();
            parentTaskEntity.setChildCount(parentTaskEntity.getChildCount() + 1);
            task = Task.builder()
                    .title(createTaskRequestDto.getTitle())
                    .childCompleteCount(0)
                    .childCount(0)
                    .description(createTaskRequestDto.getDescription())
                    .parentTask(parentTaskEntity)
                    .depth(parentTask.get().getDepth() + 1)
                    .endDate(createTaskRequestDto.getEndDate())
                    .startDate(createTaskRequestDto.getStartDate())
                    .status(createTaskRequestDto.getStatus())
                    .project(project).build();
        } else {
            project.setChildCount(project.getChildCount() + 1);
            task = Task.builder()
                    .title(createTaskRequestDto.getTitle())
                    .childCompleteCount(0)
                    .childCount(0)
                    .depth(0)
                    .description(createTaskRequestDto.getDescription())
                    .endDate(createTaskRequestDto.getEndDate())
                    .startDate(createTaskRequestDto.getStartDate())
                    .status(createTaskRequestDto.getStatus())
                    .project(project).build();
        }

        // Update project start date if task start date is earlier
        if (createTaskRequestDto.getStartDate().before(project.getStartDate())) {
            project.setStartDate(createTaskRequestDto.getStartDate());
        }

        // Update project end date if task end date is later
        if (createTaskRequestDto.getEndDate().after(project.getEndDate())) {
            project.setEndDate(createTaskRequestDto.getEndDate());
        }

        projectRepository.save(project);
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
                        .id(task.getId())
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
}
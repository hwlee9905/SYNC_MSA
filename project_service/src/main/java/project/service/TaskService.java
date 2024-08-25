package project.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.service.dto.request.CreateTaskRequestDto;
import project.service.dto.request.UpdateTaskRequestDto;
import project.service.dto.response.GetMemberFromTaskResponseDto;
import project.service.dto.response.GetTaskResponseDto;
import project.service.entity.Project;
import project.service.entity.Task;
import project.service.entity.UserTask;
import project.service.entity.UserTaskId;
import project.service.global.SuccessResponse;
import project.service.kafka.event.TaskDeleteEvent;
import project.service.kafka.event.TaskUpdateEvent;
import project.service.kafka.event.UserAddToTaskEvent;
import project.service.repository.ProjectRepository;
import project.service.repository.TaskRepository;
import project.service.repository.UserTaskRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserTaskRepository userTaskRepository;
    @Transactional(rollbackFor = { Exception.class })
    public void createTask(CreateTaskRequestDto createTaskRequestDto) {
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
                .status(0)
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
                .status(0)
                .project(project).build();
        }
        taskRepository.save(task);
    }
    
    @Transactional(rollbackFor = { Exception.class })
    public SuccessResponse getOnlyChildrenTasks(Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found with ID: " + taskId));
        GetTaskResponseDto result = GetTaskResponseDto.fromEntityOnlyChildrenTasks(task);
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
    }
    private void updateChildCompleteCountForProject(Project project, int oldStatus, int newStatus) {
        Optional.of(project)
                .filter(proj -> oldStatus != 2 && newStatus == 2)
                .ifPresent(proj -> proj.setChildCompleteCount(proj.getChildCompleteCount() + 1));

        Optional.of(project)
                .filter(proj -> oldStatus == 2 && newStatus != 2)
                .ifPresent(proj -> proj.setChildCompleteCount(proj.getChildCompleteCount() - 1));
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
}
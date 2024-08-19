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
            task = Task.builder().title(createTaskRequestDto.getTitle())
                .description(createTaskRequestDto.getDescription())
                .parentTask(parentTask.get())
                .depth(parentTask.get().getDepth() + 1)
                .endDate(createTaskRequestDto.getEndDate())
                .startDate(createTaskRequestDto.getStartDate())
                .status(createTaskRequestDto.getStatus())
                .project(project).build();
        } else {
            task = Task.builder().title(createTaskRequestDto.getTitle())
                .depth(0)
                .description(createTaskRequestDto.getDescription()).endDate(createTaskRequestDto.getEndDate())
                .startDate(createTaskRequestDto.getStartDate()).status(createTaskRequestDto.getStatus())
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
        //task id 존재하지 않는경우 예외처리 해야함 (추가)
        taskRepository.delete(task.get());
    }

    public void updateTask(TaskUpdateEvent event) {
        UpdateTaskRequestDto updateTaskRequestDto = event.getUpdateTaskRequestDto();
        Optional<Task> task = taskRepository.findById(updateTaskRequestDto.getTaskId());
        //task id 존재하지 않는경우 예외처리 해야함 (추가)
        task.get().setTitle(updateTaskRequestDto.getTitle());
        task.get().setDescription(updateTaskRequestDto.getDescription());
        task.get().setStartDate(updateTaskRequestDto.getStartDate());
        task.get().setEndDate(updateTaskRequestDto.getEndDate());
        task.get().setStatus(updateTaskRequestDto.getStatus());
        taskRepository.save(task.get());
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
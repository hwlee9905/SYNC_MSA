package project.service;

import jakarta.persistence.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.service.dto.request.CreateTaskRequestDto;
import project.service.dto.request.UpdateProjectRequestDto;
import project.service.dto.request.UpdateTaskRequestDto;
import project.service.dto.response.GetMemberFromTaskResponseDto;
import project.service.dto.response.GetTaskResponseDto;
import project.service.entity.Project;
import project.service.entity.Task;
import project.service.entity.UserTask;
import project.service.entity.UserTaskId;
import project.service.global.ResponseMessage;
import project.service.kafka.event.ProjectUpdateEvent;
import project.service.kafka.event.TaskDeleteEvent;
import project.service.kafka.event.TaskUpdateEvent;
import project.service.kafka.event.UserAddToTaskEvent;
import project.service.repository.ProjectRepository;
import project.service.repository.TaskRepository;
import project.service.repository.UserTaskRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserTaskRepository userTaskRepository;
    @Transactional(rollbackFor = { Exception.class })
    public void createTask(CreateTaskRequestDto createTaskRequestDto) {
        Optional<Project> project = projectRepository.findById(createTaskRequestDto.getProjectId());
        //project id 존재하지 않는경우 예외처리 해야함 (추가)
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
                    .project(project.get()).build();
        } else {
            task = Task.builder().title(createTaskRequestDto.getTitle())
                    .depth(0)
                    .description(createTaskRequestDto.getDescription()).endDate(createTaskRequestDto.getEndDate())
                    .startDate(createTaskRequestDto.getStartDate()).status(createTaskRequestDto.getStatus())
                    .project(project.get()).build();
        }
        taskRepository.save(task);
    }
    @Transactional(rollbackFor = { Exception.class })
    public ResponseMessage getOnlyChildrenTasks(Long taskId) {
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (!taskOptional.isPresent()) {
            return ResponseMessage.builder()
                    .result(false)
                    .build();
        }else{
            Task task = taskOptional.get();
            GetTaskResponseDto result = GetTaskResponseDto.fromEntityOnlyChildrenTasks(task);
            return ResponseMessage.builder()
                    .result(true)
                    .value(result).build();
        }

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

    public ResponseMessage getUserIdsFromTask(Long taskId) {
        List<UserTask> userTasks = userTaskRepository.findByTaskId(taskId);
        GetMemberFromTaskResponseDto result = GetMemberFromTaskResponseDto.builder()
            .userIds(userTasks.stream()
                .map(userTask -> userTask.getId().getUserId())
                .collect(Collectors.toList()))
            .build();
        if (userTasks.isEmpty()) {
            return ResponseMessage.builder()
                .result(false)
                .message("해당 업무에는 배정된 담당자가 없습니다.")
                .build();
        }
        return ResponseMessage.builder()
            .result(true)
            .value(result)
            .build();
    }
}
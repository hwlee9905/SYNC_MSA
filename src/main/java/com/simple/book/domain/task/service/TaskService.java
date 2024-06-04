package com.simple.book.domain.task.service;

import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.project.repository.ProjectRepository;
import com.simple.book.domain.task.dto.request.CreateTaskRequestDto;
import com.simple.book.domain.task.dto.response.GetTaskResponseDto;
import com.simple.book.domain.task.entity.Task;
import com.simple.book.domain.task.repository.TaskRepository;
import com.simple.book.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;

    @Transactional(rollbackFor = {Exception.class})
    public String createTask(CreateTaskRequestDto createTaskRequestDto){
        Optional<Project> project = projectRepository.findById(createTaskRequestDto.getProjectId());
        if (project.isPresent()){
            Optional<Task> parentTask = taskRepository.findById(createTaskRequestDto.getParentTaskId());
            if (parentTask.isPresent()){
                Task task = Task.builder()
                        .title(createTaskRequestDto.getTitle())
                        .description(createTaskRequestDto.getDescription())
                        .parentTask(parentTask.get())
                        .endDate(createTaskRequestDto.getEndDate())
                        .startDate(createTaskRequestDto.getStartDate())
                        .status(createTaskRequestDto.getStatus())
                        .project(project.get())
                        .build();
                taskRepository.save(task);
            }else {
                Task task = Task.builder()
                        .title(createTaskRequestDto.getTitle())
                        .description(createTaskRequestDto.getDescription())
                        .endDate(createTaskRequestDto.getEndDate())
                        .startDate(createTaskRequestDto.getStartDate())
                        .status(createTaskRequestDto.getStatus())
                        .project(project.get())
                        .build();
                taskRepository.save(task);
            }
        }
        else{
            throw new EntityNotFoundException("해당 프로젝트는 존재하지 않습니다. ProjectId : " + createTaskRequestDto.getProjectId());
        }


        return "OK";
    }


    @Transactional(rollbackFor = {Exception.class})
    public GetTaskResponseDto getAllSubTask(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("해당 업무는 존재하지 않습니다. TaskId : " + taskId));
        return GetTaskResponseDto.fromEntity(task);
    }

    @Transactional(rollbackFor = {Exception.class})
    public GetTaskResponseDto getOnlyChildrenTasks(Long taskId) {
        Task task = taskRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("해당 업무는 존재하지 않습니다. TaskId : " + taskId));
        return GetTaskResponseDto.fromEntityOnlyChildrenTasks(task);
    }
}

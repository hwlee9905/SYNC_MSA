package com.simple.book.domain.task.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.project.repository.ProjectRepository;
import com.simple.book.domain.task.dto.request.CreateTaskRequestDto;
import com.simple.book.domain.task.dto.response.GetTaskResponseDto;
import com.simple.book.domain.task.entity.Task;
import com.simple.book.domain.task.repository.TaskRepository;
import com.simple.book.global.advice.ResponseMessage;
import com.simple.book.global.exception.EntityNotFoundException;
import com.simple.book.global.exception.UnknownException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskService {
	private final TaskRepository taskRepository;
	private final ProjectRepository projectRepository;

	@Transactional(rollbackFor = { Exception.class })
	public ResponseMessage createTask(CreateTaskRequestDto createTaskRequestDto) {
		Optional<Project> project = projectRepository.findById(createTaskRequestDto.getProjectId());
		if (project.isPresent()) {
			Optional<Task> parentTask = taskRepository.findById(createTaskRequestDto.getParentTaskId());
			Task task;
			if (parentTask.isPresent()) {
				task = Task.builder().title(createTaskRequestDto.getTitle())
						.description(createTaskRequestDto.getDescription()).parentTask(parentTask.get())
						.endDate(createTaskRequestDto.getEndDate()).startDate(createTaskRequestDto.getStartDate())
						.status(createTaskRequestDto.getStatus()).project(project.get()).build();
			} else {
				task = Task.builder().title(createTaskRequestDto.getTitle())
						.description(createTaskRequestDto.getDescription()).endDate(createTaskRequestDto.getEndDate())
						.startDate(createTaskRequestDto.getStartDate()).status(createTaskRequestDto.getStatus())
						.project(project.get()).build();
			}
			try {
				taskRepository.saveAndFlush(task);
			} catch (Exception e) {
				throw new UnknownException(e.getMessage());
			}
		} else {
			throw new EntityNotFoundException("해당 프로젝트는 존재하지 않습니다. ProjectId : " + createTaskRequestDto.getProjectId());
		}
		return ResponseMessage.builder().message("success").build();
	}

	@Transactional(rollbackFor = { Exception.class })
	public ResponseMessage getAllSubTask(Long taskId) {
		Task task = taskRepository.findById(taskId)
				.orElseThrow(() -> new EntityNotFoundException("해당 업무는 존재하지 않습니다. TaskId : " + taskId));
		GetTaskResponseDto result = GetTaskResponseDto.fromEntity(task);
		return ResponseMessage.builder().value(result).build();
	}

	@Transactional(rollbackFor = { Exception.class })
	public ResponseMessage getOnlyChildrenTasks(Long taskId) {
		Task task = taskRepository.findById(taskId)
				.orElseThrow(() -> new EntityNotFoundException("해당 업무는 존재하지 않습니다. TaskId : " + taskId));
		GetTaskResponseDto result = GetTaskResponseDto.fromEntityOnlyChildrenTasks(task);
		return ResponseMessage.builder().value(result).build();
	}
}

package com.simple.book.domain.task.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simple.book.domain.alarm.service.AlarmService;
import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.project.repository.ProjectRepository;
import com.simple.book.domain.task.dto.request.CreateTaskRequestDto;
import com.simple.book.domain.task.dto.response.GetTaskResponseDto;
import com.simple.book.domain.task.entity.Task;
import com.simple.book.domain.task.repository.TaskRepository;
import com.simple.book.global.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class TaskService {
	private final TaskRepository taskRepository;
	private final ProjectRepository projectRepository;

	@Autowired
	private AlarmService alarmService;

	@Transactional(rollbackFor = { Exception.class })
	public String createTask(CreateTaskRequestDto createTaskRequestDto) {
		boolean isSuccess = false;
		long id;
		Optional<Project> project = projectRepository.findById(createTaskRequestDto.getProjectId());
		if (project.isPresent()) {
			Optional<Task> parentTask = taskRepository.findById(createTaskRequestDto.getParentTaskId());
			if (parentTask.isPresent()) {
				Task task = Task.builder()
						.title(createTaskRequestDto.getTitle())
						.description(createTaskRequestDto.getDescription())
						.parentTask(parentTask.get())
						.endDate(createTaskRequestDto.getEndDate())
						.startDate(createTaskRequestDto.getStartDate())
						.status(createTaskRequestDto.getStatus())
						.project(project.get())
						.build();
				try {
					id = taskRepository.save(task).getId();
					isSuccess = true;
				} catch (Exception e) {
					throw new RuntimeException("시스템 오류가 발생하였습니다.", e);
				}
			} else {
				Task task = Task.builder().title(createTaskRequestDto.getTitle())
						.description(createTaskRequestDto.getDescription()).endDate(createTaskRequestDto.getEndDate())
						.startDate(createTaskRequestDto.getStartDate()).status(createTaskRequestDto.getStatus())
						.project(project.get()).build();
				try {
					id = taskRepository.save(task).getId();
					isSuccess = true;
				} catch (Exception e) {
					throw new RuntimeException("시스템 오류가 발생하였습니다.", e);
				}
			}
		} else {
			throw new EntityNotFoundException("해당 프로젝트는 존재하지 않습니다. ProjectId : " + createTaskRequestDto.getProjectId());
		}

		if (isSuccess) {
			alarmService.sendTaskManager(createTaskRequestDto.getMemberId());
		}

		return "OK";
	}

	@Transactional(rollbackFor = { Exception.class })
	public GetTaskResponseDto getAllSubTask(Long taskId) {
		Task task = taskRepository.findById(taskId)
				.orElseThrow(() -> new EntityNotFoundException("해당 업무는 존재하지 않습니다. TaskId : " + taskId));
		return GetTaskResponseDto.fromEntity(task);
	}

	@Transactional(rollbackFor = { Exception.class })
	public GetTaskResponseDto getOnlyChildrenTasks(Long taskId) {
		Task task = taskRepository.findById(taskId)
				.orElseThrow(() -> new EntityNotFoundException("해당 업무는 존재하지 않습니다. TaskId : " + taskId));
		return GetTaskResponseDto.fromEntityOnlyChildrenTasks(task);
	}
}

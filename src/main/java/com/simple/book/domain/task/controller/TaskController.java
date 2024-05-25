package com.simple.book.domain.task.controller;

import com.simple.book.domain.task.dto.request.CreateTaskRequestDto;
import com.simple.book.domain.task.dto.request.GetTaskRequestDto;
import com.simple.book.domain.task.dto.response.GetTaskResponseDto;
import com.simple.book.domain.task.entity.Task;
import com.simple.book.domain.task.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@Controller
@RequestMapping("api/user/project/task")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    @PostMapping("/create")
    public String createTask(@RequestBody CreateTaskRequestDto createTaskRequestDto) {
        return taskService.createTask(createTaskRequestDto);
    }
    //해당 업무의 모든 하위 업무를 조회합니다.
    @PostMapping("/getSubTasks")
    public GetTaskResponseDto getSubTasks(@RequestBody GetTaskRequestDto getTaskRequestDto) {
        return taskService.getAllSubTask(getTaskRequestDto.getTaskId());
    }
    //해당 업무의 자식 업무만 조회합니다.
    @PostMapping("/getOnlyChildrenTasks")
    public GetTaskResponseDto getOnlyChildrenTasks(@RequestBody GetTaskRequestDto getTaskRequestDto) {
        return taskService.getOnlyChildrenTasks(getTaskRequestDto.getTaskId());
    }
}

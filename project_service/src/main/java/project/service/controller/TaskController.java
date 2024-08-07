package project.service.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.service.TaskService;
import project.service.dto.request.GetMemberFromTaskRequestDto;
import project.service.dto.request.GetTaskRequestDto;
import project.service.global.ResponseMessage;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    private final TaskService taskService;
    @GetMapping("project/tasks/api/v1/getChildren")
    public ResponseMessage getOnlyChildrenTasks(GetTaskRequestDto getTaskRequestDto)  {
        return taskService.getOnlyChildrenTasks(getTaskRequestDto.getTaskId());
    }
    @GetMapping("/project/task/api/v1/users")
    public ResponseMessage getUserFromTask(@RequestParam Long taskId) {
        return taskService.getUserIdsFromTask(taskId);
    }
    //jwtTEST
    @GetMapping("/user/api/test")
    public void projectAPITest() {
        log.info("projectAPITest");
    }
}

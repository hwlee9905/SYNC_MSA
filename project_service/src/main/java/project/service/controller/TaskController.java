package project.service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.service.TaskService;
import project.service.dto.request.GetTaskRequestDto;
import project.service.global.SuccessResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    private final TaskService taskService;
    @GetMapping("/api/task/OnlyChildrenTasks")
    public SuccessResponse getOnlyChildrenTasks(@RequestParam Long taskId)  {
        //progress 로직 추가
        return taskService.getOnlyChildrenTasks(taskId);
    }
    @GetMapping("/project/task/api/v1/users")
    public SuccessResponse getUserFromTask(@RequestParam Long taskId) {
        return taskService.getUserIdsFromTask(taskId);
    }
    //jwtTEST
    @GetMapping("/user/api/test")
    public void projectAPITest() {
        log.info("projectAPITest");
    }
    @GetMapping("/api/task")
    public SuccessResponse getTask(@RequestParam Long taskId) {
        return taskService.getTask(taskId);
    }
    @GetMapping("/api/task/images")
    public SuccessResponse getImages(@RequestParam List<String> filenames) {
        return taskService.getImages(filenames);
    }
}

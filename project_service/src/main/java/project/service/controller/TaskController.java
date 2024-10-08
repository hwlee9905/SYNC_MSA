package project.service.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    @GetMapping("node2/api/task/v1")
    public SuccessResponse getOnlyChildrenTasks(@RequestParam Long taskId)  {
        //progress 로직 추가
        return taskService.getOnlyChildrenTasks(taskId);
    }
    @GetMapping("node2/api/task/v2")
    public SuccessResponse getTasksByProjectId(@RequestParam Long projectId)  {
        return taskService.getTaskByProjectId(projectId);
    }
    @GetMapping("node2/api/task/v4")
    public SuccessResponse getUsersFromTask(@RequestParam Long taskId) {
        return taskService.getUserIdsFromTask(taskId);
    }
    //jwtTEST
    @GetMapping("node2/user/api/test")
    public void projectAPITest() {
        log.info("projectAPITest");
    }
    @GetMapping("node2/api/task/v3")
    public SuccessResponse getTask(@RequestParam Long taskId) {
        return taskService.getTask(taskId);
    }
    @GetMapping("node2/api/task/image")
    public ResponseEntity<Resource> getImage(@RequestParam String filename) {
        return taskService.getImage(filename);
    }
    @PostMapping("/task/test")
    public void testproject(@RequestParam Long taskId, @RequestParam Long userId) {
        taskService.removeUserFromTaskv2(userId, taskId);
    }
}

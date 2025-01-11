package project.service.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.service.TaskService;
import project.service.global.SuccessResponse;

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
        Resource file = taskService.getImage(filename).getBody(); // getImage 메서드가 Resource 객체 반환
        String contentType = "image/jpeg"; // 적절한 MIME 타입으로 설정
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(file);
    }
}

package user.service.web;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import user.service.global.advice.SuccessResponse;
import user.service.kafka.task.KafkaTaskProducerService;
import user.service.web.dto.task.request.*;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    private final KafkaTaskProducerService kafkaTaskProducerService;
    @Operation(summary = "업무를 생성하기 위한 API", description = "HOST = 150.136.153.235:30080 <br>" +
            "ValidationDetails : CreateTaskRequestDto <br>" +
            "depth는 parentTask의 depth에 따라 결정 되며, 최상위 업무는 0, 그 하위 업무는 1, 그 하위 업무는 2로 결정됩니다. parentTask의 depth가 2일 경우, 생성되지 않습니다.")
    @PostMapping("user/api/task/v1")
    public SuccessResponse createTask(@RequestPart("data") CreateTaskRequestDto createTaskRequestDto, @RequestPart(value = "images", required = false) List<MultipartFile> descriptionImages, @RequestPart(value = "titleimage", required = false) List<MultipartFile> titleImage) throws IOException {
        return kafkaTaskProducerService.sendCreateTaskEvent(createTaskRequestDto,descriptionImages);
    }
//    public void createTask(HttpServletRequest request) throws ServletException, IOException {
//        log.info("request : {}", request);
//        Collection<Part> parts = request.getParts();
//        log.info("parts={}", parts);
////        return kafkaTaskProducerService.sendCreateTaskEvent(createTaskRequestDto,descriptionImages);
//    }
    //해당 업무의 자식 업무만 조회합니다.
    @Operation(summary = "해당 업무의 자식 업무를 조회하기 위한 API", description = "HOST = 150.136.153.235:31585 <br>" +
            "ValidationDetails : GetTaskRequestDto")
    @GetMapping("api/task/OnlyChildrenTasks")
    public void getOnlyChildrenTasks(@RequestBody @Valid GetTaskRequestDto getTaskRequestDto) {
    }
    //해당 업무를 삭제합니다.
    @Operation(summary = "업무를 삭제하기 위한 API", description = "HOST = 150.136.153.235:30080 <br>" +
            "ValidationDetails : DeleteTaskRequestDto")
    @DeleteMapping("/user/api/task")
    public SuccessResponse deleteTask(@RequestBody @Valid DeleteTaskRequestDto deleteTaskRequestDto) {
        return kafkaTaskProducerService.sendDeleteTaskEvent(deleteTaskRequestDto);
    }
    @Operation(summary = "업무를 수정하기 위한 API", description = "HOST = 150.136.153.235:30080")
    @PutMapping("/user/api/task")
    public SuccessResponse updateTask(@RequestBody @Valid UpdateTaskRequestDto updateTaskRequestDto) {
        //업무 업데이트 이벤트 생성 로직 추가
        return kafkaTaskProducerService.sendUpdateTaskEvent(updateTaskRequestDto);
    }

}

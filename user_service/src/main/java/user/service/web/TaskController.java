package user.service.web;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import user.service.FileValidationService;
import user.service.global.advice.SuccessResponse;
import user.service.kafka.task.KafkaTaskProducerService;
import user.service.web.dto.task.request.*;
import java.io.IOException;
import java.util.List;
@RestController
@RequiredArgsConstructor
@Slf4j
public class TaskController {
    private final FileValidationService fileValidationService;
    private final KafkaTaskProducerService kafkaTaskProducerService;
    @Operation(summary = "업무를 생성하기 위한 API", description = "HOST = 150.136.153.235:30080 <br>" +
            "Validation : 로그인 필요함, 해당 프로젝트에 속해있지 않은 유저는 업무 생성 불가, filename의 uuid 검사 <br>" +
            "DTOValidationDetails : CreateTaskRequestDto <br>" +
            "depth는 parentTask의 depth에 따라 결정 되며, 최상위 업무는 0, 그 하위 업무는 1, 그 하위 업무는 2로 결정됩니다. parentTask의 depth가 2일 경우, 생성되지 않습니다. <br>" +
            "description에 img가 포함되어 있을 경우, multipart를 사용해 이미지와 json data를 함께 보내야 합니다. <br>" +
            "content-Type : multipart/form-data; boundary=<calculated when request is sent> <br>" +
            "body : form-data <br>" +
            "key : images, value : description에 포함될 이미지 <br>" +
            "key: data, value : CreateTaskRequestDto <br>")
    @PostMapping("user/api/task/v1")
    public SuccessResponse createTask(
            @RequestPart("data") CreateTaskRequestDto createTaskRequestDto,
            @RequestPart(value = "images", required = false) List<MultipartFile> descriptionImages,
            @RequestPart(value = "titleimage", required = false) MultipartFile titleImage) throws IOException {
        //확장자, uuid validaiton
        if (descriptionImages != null) {
            for (MultipartFile image : descriptionImages) {
                fileValidationService.validateImageFile(image);
            }
        }
        if (titleImage != null) {
            fileValidationService.validateImageFile(titleImage);
        }
        //titleimage 추가 할 것
        return kafkaTaskProducerService.sendCreateTaskEvent(createTaskRequestDto, descriptionImages);
    }
//    public void createTask(HttpServletRequest request) throws ServletException, IOException {
//        log.info("request : {}", request);
//        Collection<Part> parts = request.getParts();
//        log.info("parts={}", parts);
////        return kafkaTaskProducerService.sendCreateTaskEvent(createTaskRequestDto,descriptionImages);
//    }
    //해당 업무의 자식 업무만 조회합니다.
    @Operation(summary = "해당 업무의 자식 업무를 조회하기 위한 API", description = "HOST = 150.136.153.235:31585 <br>" +
            "Validation : 로그인 필요하지 않음, 잘못된 taskId 입력시 오류 발생 <br>" +
            "ResponseDto : GetTasksResponseDto <br>")
    @GetMapping("api/task/v1")
    public void getOnlyChildrenTasks(@RequestParam Long taskId) {
    }
    @Operation(summary = "해당 프로젝트의 업무를 조회하기 위한 API", description = "HOST = 150.136.153.235:31585 <br>" +
            "Validation : 로그인 필요하지 않음, 잘못된 taskId 입력시 오류 발생 <br>" +
            "ResponseDto : GetTasksByProjectIdResponseDto")
    @GetMapping("/api/task/v2")
    public void getTasksByProjectId(@RequestParam Long projectId)  {
    }
    //해당 업무를 삭제합니다.
//    @Operation(summary = "업무를 삭제하기 위한 API", description = "HOST = 150.136.153.235:30080 <br>" +
//            "ValidationDetails : DeleteTaskRequestDto")
//    @DeleteMapping("/user/api/task")
//    public SuccessResponse deleteTask(@RequestBody @Valid DeleteTaskRequestDto deleteTaskRequestDto) {
//        return kafkaTaskProducerService.sendDeleteTaskEvent(deleteTaskRequestDto);
//    }
    @Operation(summary = "업무를 수정하기 위한 API", description = "HOST = 150.136.153.235:30080 <br>" +
            "Validation : 로그인 필요함, 해당 프로젝트에 속해있지 않은 유저는 업무 수정 불가 <br>" +
            "DTOValidation : UpdateTaskRequestDto")
    @PutMapping("/user/api/task")
    public SuccessResponse updateTask(@RequestBody @Valid UpdateTaskRequestDto updateTaskRequestDto) {
        //업무 업데이트 이벤트 생성 로직 추가
        return kafkaTaskProducerService.sendUpdateTaskEvent(updateTaskRequestDto);
    }
    @Operation(summary = "파일을 가져오기 위한 API", description = "HOST = 150.136.153.235:31585 <br>"
            + "Validation : 로그인 필요하지 않음, 잘못된 filename 입력시 오류 발생")
    @GetMapping("/api/task/image")
    public void getImage(@RequestParam String filename) {

    }
    @Operation(summary = "이미지를 포함한 단일 task를 가져오는 API", description = "HOST = 150.136.153.235:30080"
            + "Validation : 로그인 필요하지 않음, 잘못된 taskId 입력시 오류 발생")
    @GetMapping("/api/task/v3")
    public void getTask(@RequestParam Long taskId) {

    }
}

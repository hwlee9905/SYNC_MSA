package user.service.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import user.service.MemberService;
import user.service.UserService;
import user.service.global.advice.LogAop;
import user.service.web.dto.project.request.*;
import user.service.global.advice.SuccessResponse;
import user.service.kafka.project.KafkaProjectProducerService;
import java.util.List;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class ProjectController {
    private final UserService userService;
    private final KafkaProjectProducerService kafkaProducerService;
    private final MemberService memberService;
    
    @Operation(summary = "프로젝트를 생성하기 위한 API", description = "HOST = 150.136.153.235:30443 <br>" +
            "ValidationDetails : CreateProjectRequestDto")
    @PostMapping("/user/api/project")
    @LogAop
    public ResponseEntity<SuccessResponse> createProject(@RequestPart("data") @Valid CreateProjectRequestDto projectCreateRequestDto, @RequestPart(value = "thumbnailImage", required = false) MultipartFile img) {
        String userId = userService.getCurrentUserId();
        kafkaProducerService.sendCreateProjectEvent(projectCreateRequestDto, img, userId);
        return ResponseEntity.ok().body(SuccessResponse.builder().message("프로젝트 생성 이벤트 생성").build());
    }
    
    @Operation(summary = "프로젝트를 삭제하기 위한 API", description = "HOST = 150.136.153.235:30443 <br>" +
        "ValidationDetails : DeleteProjectRequestDto")
    @DeleteMapping("/user/api/project")
    @LogAop
    public ResponseEntity<SuccessResponse> deleteProject(@RequestBody @Valid DeleteProjectRequestDto projectDeleteRequestDto) {
        String userId = userService.getCurrentUserId();
        kafkaProducerService.sendDeleteProjectEvent(projectDeleteRequestDto, userId);
        return ResponseEntity.ok().body(SuccessResponse.builder().message("프로젝트 삭제 이벤트 생성").build());
    }
    
    //optional로 수정
    @Operation(summary = "프로젝트를 수정하기 위한 API", description = "HOST = 150.136.153.235:30080 <br>" +
            "ValidationDetails : UpdateProjectRequestDto")
    @PutMapping("/user/api/project")
    @LogAop
    public ResponseEntity<SuccessResponse> updateProject(@RequestBody @Valid UpdateProjectRequestDto updateProjectRequestDto, @RequestPart(value = "thumbnailImage", required = false) MultipartFile img) {
        kafkaProducerService.updateProject(updateProjectRequestDto, img);
        return ResponseEntity.ok().body(SuccessResponse.builder().message("프로젝트 업데이트 이벤트 생성").build());
    }
    
    @Operation(summary = "프로젝트들의 정보를 가져오기 위한 API", description = "HOST = 150.136.153.235:30443")
    @GetMapping("/node2/project/api/v1")
    @LogAop
    public void getProjects(@Parameter(description = "존재하지 않는 프로젝트 아이디 입력시 오류 발생") @RequestParam List<Long> projectIds) {
    }
    
    @Operation(summary = "유저가 속해있는 프로젝트들의 ID를 가져오기 위한 API", description = "HOST = 150.136.153.235:30443")
    @GetMapping("/project/api/v2")
    @LogAop
    public ResponseEntity<SuccessResponse> getProjectsByUserLoginId(@Parameter(description = "존재하지 않는 로그인 아이디 입력시 오류 발생") @RequestParam(name="userId") String userId) {
        Long userEntityId = userService.getUserEntityId(userId);
        return ResponseEntity.ok(memberService.getProjectIdsByUserId(userEntityId));
    }
}

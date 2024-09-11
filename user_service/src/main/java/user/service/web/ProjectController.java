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
    public ResponseEntity<SuccessResponse> createProject(@RequestBody @Valid CreateProjectRequestDto projectCreateRequestDto) {
        String userId = userService.getCurrentUserId();
        kafkaProducerService.sendCreateProjectEvent(projectCreateRequestDto, userId);
        return ResponseEntity.ok().body(SuccessResponse.builder().message("프로젝트 생성 이벤트 생성").build());
    }
    
    @Operation(summary = "프로젝트를 삭제하기 위한 API", description = "HOST = 150.136.153.235:30443 <br>" +
        "ValidationDetails : DeleteProjectRequestDto")
    @DeleteMapping("/user/api/project")
    public ResponseEntity<SuccessResponse> deleteProject(@RequestBody @Valid DeleteProjectRequestDto projectDeleteRequestDto) {
        String userId = userService.getCurrentUserId();
        kafkaProducerService.sendDeleteProjectEvent(projectDeleteRequestDto, userId);
        return ResponseEntity.ok().body(SuccessResponse.builder().message("프로젝트 삭제 이벤트 생성").build());
    }
    
        //optional로 수정
    @Operation(summary = "프로젝트를 수정하기 위한 API", description = "HOST = 150.136.153.235:30443 <br>" +
            "ValidationDetails : UpdateProjectRequestDto")
    @PutMapping("/user/api/project")
    public ResponseEntity<SuccessResponse> updateProject(@RequestBody @Valid UpdateProjectRequestDto updateProjectRequestDto) {
        kafkaProducerService.updateProject(updateProjectRequestDto);
        return ResponseEntity.ok().body(SuccessResponse.builder().message("프로젝트 업데이트 이벤트 생성").build());
    }
    
    @Operation(summary = "프로젝트들의 정보를 가져오기 위한 API", description = "HOST = 150.136.153.235:30443")
    @GetMapping("/node2/project/api/v1")
    public void getProjects(@Parameter(description = "존재하지 않는 프로젝트 아이디 입력시 오류 발생") @RequestParam List<Long> projectIds) {
    }
    
    @Operation(summary = "유저가 속해있는 프로젝트들의 ID를 가져오기 위한 API", description = "HOST = 150.136.153.235:30443")
    @GetMapping("/project/api/v2")
    public ResponseEntity<SuccessResponse> getProjectsByUserLoginId(@Parameter(description = "존재하지 않는 로그인 아이디 입력시 오류 발생") @RequestParam String userId) {
        Long userEntityId = userService.getUserEntityId(userId);
        return ResponseEntity.ok(memberService.getProjectIdsByUserId(userEntityId));
    }
    
    /**
     * 프로젝트 대표 이미지 저장
     * @param body
     * @return
     */
    @Operation(summary = "프로젝트 생성 시, 대표 이미지 저장 API", description = "HOST = 150.136.153.235:30443 <br>" +
            "ValidationDetails : AddProjectImgDto")
    @PostMapping("/user/api/project/add/img")
    public ResponseEntity<SuccessResponse> addThumbnail(@RequestParam("thumbnail") MultipartFile thumbnail) {
    	String userId = userService.getCurrentUserId();
    	kafkaProducerService.sendAddProjectImgEvent(thumbnail, userId);
    	return ResponseEntity.ok(SuccessResponse.builder().message("프로젝트 이미지 저장 이벤트 생성").build());
    }
    
    /**
     * 프로젝트 대표 아이콘 저장
     * @param body
     * @return
     */
    @Operation(summary = "프로젝트 생성 시, 대표 아이콘 저장 API", description = "HOST = 150.136.153.235:30443 <br>" +
            "ValidationDetails : AddProjectIconDto")
    @PostMapping("/user/api/project/add/icon")
    public ResponseEntity<SuccessResponse> addThumbnail(@RequestBody @Valid AddProjectIconDto body) {
    	String userId = userService.getCurrentUserId();
    	kafkaProducerService.sendAddProjectIconEvent(body, userId);
    	return ResponseEntity.ok(SuccessResponse.builder().message("프로젝트 아이콘 저장 이벤트 생성").build());
    }
    
}

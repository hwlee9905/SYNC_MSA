package project.service;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import project.service.dto.request.CreateProjectRequestDto;
import project.service.dto.request.UpdateProjectRequestDto;
import project.service.dto.response.GetProjectsResponseDto;
import project.service.entity.Project;
import project.service.global.SuccessResponse;
import project.service.global.util.FileManagement;
import project.service.kafka.event.ProjectDeleteEvent;
import project.service.kafka.event.ProjectUpdateEvent;
import project.service.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {
	private final ProjectRepository projectRepository;
	private final FileManagement fileManagement;
	
	@Transactional(rollbackFor = { Exception.class })
	public Project createProject(CreateProjectRequestDto projectCreateRequestDto, byte[] img, String extsn) {
		Project project = new Project();
		project.setTitle(projectCreateRequestDto.getTitle());
		project.setDescription(projectCreateRequestDto.getDescription());
		project.setSubTitle(projectCreateRequestDto.getSubTitle());
		project.setStartDate(projectCreateRequestDto.getStartDate());
		project.setEndDate(projectCreateRequestDto.getEndDate());
		
		/**
    	 * 'I' : 이미지
    	 * 'C' : 아이콘
    	 * 'E' : 이모지
    	 * 'N' : 없음
    	 */
		String thumbnail;
		char thumbnailType = projectCreateRequestDto.getThumbnailType();
		if (thumbnailType != 'N') {
			if (thumbnailType == 'I') {			// 썸네일이 이미지일 경우
				thumbnail = UUID.randomUUID().toString() + "." + extsn;
				// 만약 Exception 발생하면 저장된 썸네일 이미지도 삭제 시켜야함 (롤백)
				// 나중에 개발...ㅋㅋ...
				fileManagement.uploadThumbnail(img, thumbnail, 'P');
				project.setThumbnail(thumbnail);
			} else {							// 썸네일이 아이콘 또는 이모지일 경우
				thumbnail = projectCreateRequestDto.getThumbnail();
				project.setThumbnail(thumbnail);
			}
			project.setThumbnail(thumbnail);
		}
		project.setThumbnailType(projectCreateRequestDto.getThumbnailType());

		return projectRepository.save(project);
	}
	
	@Transactional(rollbackFor = { Exception.class })
	public SuccessResponse findProject(Long projectId) {
		try {
			projectRepository.findById(projectId).orElseThrow(() -> new EntityNotFoundException(String.valueOf(projectId)));

			return SuccessResponse.builder().message("프로젝트 조회 성공").build();
		} catch (EntityNotFoundException e) {
			return SuccessResponse.builder().message("해당 프로젝트는 존재하지 않습니다.").result(false).data(projectId).build();
		}
	}
	
	@Transactional(rollbackFor = { Exception.class })
    public void deleteProject(ProjectDeleteEvent event) {
		Optional<Project> project = projectRepository.findById(event.getProjectId());
		//관련 task 파일 삭제
		//프로젝트가 존재하지 않을 경우 에러 처리 로직 추가
		projectRepository.delete(project.get());
    }

	@Transactional(rollbackFor = { Exception.class })
	public SuccessResponse getProjects(HttpServletRequest request, List<Long> projectIds) {
		List<GetProjectsResponseDto> result = projectIds.stream()
				.map(projectRepository::findById)
				.filter(Optional::isPresent)
				.map(Optional::get)
				.map(project -> {
					float progress = 0.0f;
					if (project.getChildCount() > 0) {
						progress = (float) project.getChildCompleteCount() / project.getChildCount();
					}
					return new GetProjectsResponseDto(
						project.getId(),
						project.getTitle(),
						project.getSubTitle(),
						project.getDescription(),
						project.getStartDate(),
						project.getEndDate(),
						project.getThumbnailType() == 'M' ? request.getScheme() + "://" +request.getServerName() + ":" + request.getServerPort() + "/project/thumbnail/" + project.getThumbnail() : project.getThumbnail(),
						project.getThumbnailType(),
						progress
					);
				})
				.collect(Collectors.toList());
		return SuccessResponse.builder().message("프로젝트 조회 완료").data(Collections.singletonMap("projectInfos", result)).build();
	}
	
	public ResponseEntity<Resource> getProjectThumbnail(String thumbnail) {
		Resource image = fileManagement.getThumbnail(thumbnail, 'P');
		HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "image/" + thumbnail.substring(thumbnail.lastIndexOf(".") + 1).toLowerCase());
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + thumbnail + "\"");
        
        return new ResponseEntity<>(image, headers, HttpStatus.OK);
	}

	@Transactional(rollbackFor = { Exception.class })
	public void updateProject(ProjectUpdateEvent event) {
		UpdateProjectRequestDto updateProjectRequestDto = event.getProjectUpdateRequestDto();
		Optional<Project> project = projectRepository.findById(updateProjectRequestDto.getProjectId());
		Project getProject = project.get();
		getProject.setDescription(updateProjectRequestDto.getDescription());
		getProject.setSubTitle(updateProjectRequestDto.getSubTitle());
		getProject.setStartDate(updateProjectRequestDto.getStartDate());
		getProject.setEndDate(updateProjectRequestDto.getEndDate());
		getProject.setTitle(updateProjectRequestDto.getTitle());
		
		if (getProject.getThumbnailType() == 'M') {
			fileManagement.deleteThumbnail(getProject.getThumbnail(), 'P');
		}
		
		String thumbnail;
		if (event.getImg() != null && event.getProjectUpdateRequestDto().getIcon() == null) {
			thumbnail = UUID.randomUUID().toString() + "." + event.getExtsn();
			fileManagement.uploadThumbnail(event.getImg(), thumbnail, 'P');
			getProject.setThumbnail(thumbnail);
			getProject.setThumbnailType('M');
		} else if(event.getProjectUpdateRequestDto().getIcon() != null && event.getImg() == null) {
			thumbnail = event.getProjectUpdateRequestDto().getIcon();
			getProject.setThumbnail(thumbnail);
			getProject.setThumbnailType('C');
		} else {
			getProject.setThumbnailType('N');
		}
		
		//projectRepository.save(getProject);
	}
}

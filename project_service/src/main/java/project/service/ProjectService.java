package project.service;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.core.io.FileSystemResource;
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
import project.service.global.config.ApplicationConfig;
import project.service.global.exception.DeleteImageFailedException;
import project.service.global.exception.ImageNotFoundException;
import project.service.global.exception.SavingImageFailedException;
import project.service.kafka.event.ProjectDeleteEvent;
import project.service.kafka.event.ProjectUpdateEvent;
import project.service.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {
	private final ProjectRepository projectRepository;
	private final ApplicationConfig applicationConfig;
	
	@Transactional(rollbackFor = { Exception.class })
	public Project createProject(CreateProjectRequestDto projectCreateRequestDto, byte[] img, String extension) {
		Project project = new Project();
		project.setTitle(projectCreateRequestDto.getTitle());
		project.setDescription(projectCreateRequestDto.getDescription());
		project.setSubTitle(projectCreateRequestDto.getSubTitle());
		project.setStartDate(projectCreateRequestDto.getStartDate());
		project.setEndDate(projectCreateRequestDto.getEndDate());
		
		String thumbnail;
		if (img != null && projectCreateRequestDto.getIcon() == null) {
			thumbnail = UUID.randomUUID().toString() + "." + extension;
			uploadThumbnail(img, thumbnail);
			project.setThumbnail(thumbnail);
			project.setThumbnailType('M');
		} else if(projectCreateRequestDto.getIcon() != null && img == null) {
			thumbnail = projectCreateRequestDto.getIcon();
			project.setThumbnail(thumbnail);
			project.setThumbnailType('C');
		} else {
			project.setThumbnailType('N');
		}

		return projectRepository.save(project);
	}
	
	private void uploadThumbnail(byte[] img, String thumbnail) {
		File outputFile = new File(applicationConfig.getImgStoragePath(), thumbnail);
		try (FileOutputStream fos = new FileOutputStream(outputFile)) {
	        fos.write(img);
	        fos.flush();
	    } catch (Exception e) {
	        throw new SavingImageFailedException(e.getMessage());
		}
	}
	
	private void deleteThumbnail(String thumbnail) {
		Path path = Paths.get(applicationConfig.getImgStoragePath(), thumbnail);
		try {
            Files.delete(path);
        } catch (Exception e) {
        	throw new DeleteImageFailedException(e.getMessage());
        }
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
		Resource image = new FileSystemResource(applicationConfig.getImgStoragePath() + thumbnail);
		if (!image.exists()) {
			throw new ImageNotFoundException(applicationConfig.getImgStoragePath() + thumbnail);
		}
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
			deleteThumbnail(getProject.getThumbnail());
		}
		
		String thumbnail;
		if (event.getImg() != null && event.getProjectUpdateRequestDto().getIcon() == null) {
			thumbnail = UUID.randomUUID().toString();
			uploadThumbnail(event.getImg(), thumbnail);
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

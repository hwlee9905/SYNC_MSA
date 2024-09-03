package project.service;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import project.service.dto.request.CreateProjectRequestDto;
import project.service.dto.request.UpdateProjectRequestDto;
import project.service.dto.response.GetProjectsResponseDto;
import project.service.entity.Project;
import project.service.global.SuccessResponse;
import project.service.kafka.event.ProjectDeleteEvent;
import project.service.kafka.event.ProjectUpdateEvent;
import project.service.repository.ProjectRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProjectService {
	private final ProjectRepository projectRepository;
	@Transactional(rollbackFor = { Exception.class })
	public Project createProject(CreateProjectRequestDto projectCreateRequestDto) {
		Project project = Project.builder()
				.description(projectCreateRequestDto.getDescription())
				.subTitle(projectCreateRequestDto.getSubTitle())
				.startDate(projectCreateRequestDto.getStartDate())
				.endDate(projectCreateRequestDto.getEndDate())
				.title(projectCreateRequestDto.getTitle()).build();
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
	public SuccessResponse getProjects(List<Long> projectIds) {

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
						progress
					);
				})
				.collect(Collectors.toList());
		return SuccessResponse.builder().message("프로젝트 조회 완료").data(result).build();
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
		projectRepository.save(getProject);
	}
}

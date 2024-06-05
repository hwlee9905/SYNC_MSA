package com.simple.book.domain.project.service;

import com.simple.book.domain.alarm.dto.req.ReqTopicDto;
import com.simple.book.domain.alarm.service.AlarmService;
import com.simple.book.domain.project.dto.request.ProjectCreateRequestDto;
import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {
	@Autowired
	private AlarmService alarmService;
	
	private final String TYPE = "P"; // project = P, task = T
	
    private final ProjectRepository projectRepository;
	
    @Transactional(rollbackFor = {Exception.class})
    public String createProject(ProjectCreateRequestDto projectCreateRequestDto){
        Project project = Project.builder()
                .description(projectCreateRequestDto.getDescription())
                .title(projectCreateRequestDto.getTitle())
                .build();
        long id = projectRepository.save(project).getId();
        alarmService.createTopic(setDto(id));
        return "OK";
    }
    
    private ReqTopicDto setDto(long id) {
    	ReqTopicDto dto = new ReqTopicDto();
    	dto.setName(TYPE + id);
    	dto.setType(TYPE);
    	dto.setId(id);
    	return dto;
    }
//    public boolean isAdmin(User user, Project project) {
//        // 프로젝트 관리자인 경우 허용
//
//    }
}

package com.simple.book.domain.project.service;

import com.simple.book.domain.project.dto.request.ProjectCreateRequestDto;
import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    @Transactional(rollbackFor = {Exception.class})
    public String createProject(ProjectCreateRequestDto projectCreateRequestDto){
        Project project = Project.builder()
                .description(projectCreateRequestDto.getDescription())
                .title(projectCreateRequestDto.getTitle())
                .build();
        projectRepository.save(project);
        return "OK";
    }
//    public boolean isAdmin(User user, Project project) {
//        // 프로젝트 관리자인 경우 허용
//
//    }
}

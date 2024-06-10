package com.simple.book.domain.project.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.simple.book.domain.member.entity.Member;
import com.simple.book.domain.member.repository.MemberRepository;
import com.simple.book.domain.project.dto.request.ProjectCreateRequestDto;
import com.simple.book.domain.project.dto.request.ProjectDeleteRequestDto;
import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.project.repository.ProjectRepository;
import com.simple.book.domain.user.entity.User;
import com.simple.book.domain.user.repository.UserRepository;
import com.simple.book.domain.user.service.UserService;
import com.simple.book.global.exception.EntityNotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final UserService userService;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;
	
    @Transactional(rollbackFor = {Exception.class})
    public String createProject(ProjectCreateRequestDto projectCreateRequestDto){
        Project project = Project.builder()
                .description(projectCreateRequestDto.getDescription())
                .title(projectCreateRequestDto.getTitle())
                .build();
        projectRepository.save(project);
        return "OK";
    }
    
    public String deleteProject(ProjectDeleteRequestDto projectDeleteRequestDto) {
        Optional<Project> project = projectRepository.findById(projectDeleteRequestDto.getProjectId());
        User user = userRepository.findByAuthenticationUserId(userService.getCurrentUserId());
        try{
            if (project.isPresent()){
                isProjectManager(user, project.get());
                projectRepository.delete(project.get());
            }else {
                throw new EntityNotFoundException("해당 프로젝트는 존재하지 않습니다. ProjectId : " + projectDeleteRequestDto.getProjectId());
            }
        }catch (Exception e) {
            throw new RuntimeException(e);
        }


        return "OK";
    }
    //프로젝트의 관리자인지 확인합니다.
    public void isProjectManager(User user, Project project) {
        Long userId = user.getId();

        Optional<Member> member = memberRepository.findByUserIdAndProjectId(userId, project.getId());

        if (member.isPresent()) {
            if (member.get().isManager()) {
                // 유효한 관리자
            } else {
                throw new EntityNotFoundException("해당 멤버는 해당 프로젝트의 관리자가 아닙니다. ProjectId : " + project.getId() + " UserId : " + user.getAuthentication().getUserId());
            }
        } else {
            throw new EntityNotFoundException("해당 멤버는 해당 프로젝트에 소속되어 있지 않습니다. ProjectId : " + project.getId() + " UserId : " + user.getAuthentication().getUserId());
        }
    }

}

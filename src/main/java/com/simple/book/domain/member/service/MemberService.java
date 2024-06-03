package com.simple.book.domain.member.service;

import com.simple.book.domain.member.dto.MemberMappingRequestDto;
import com.simple.book.domain.member.entity.Member;
import com.simple.book.domain.member.repository.MemberRepository;
import com.simple.book.domain.project.dto.request.ProjectCreateRequestDto;
import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.project.repository.ProjectRepository;
import com.simple.book.domain.user.entity.User;
import com.simple.book.domain.user.repository.UserRepository;
import com.simple.book.domain.user.service.UserService;
import com.simple.book.global.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    @Transactional(rollbackFor = {Exception.class})
    public String memberAddToProject(MemberMappingRequestDto memberMappingRequestDto) {
        try {
            Project project = findProjectById(memberMappingRequestDto.getProjectId());
            User user = findUserByCurrentUserId();

            Member member = Member.builder()
                    .project(project)
                    .isManager(memberMappingRequestDto.getIsManager())
                    .user(user)
                    .build();

            memberRepository.save(member);
            return "OK";
        } catch (Exception e) {
            // Handle other exceptions if needed
            throw new RuntimeException("An unexpected error occurred", e);
        }
    }

    private Project findProjectById(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + projectId));
    }

    private User findUserByCurrentUserId() {
        String currentUserId = userService.getCurrentUserId();
        return userRepository.findByAuthenticationUserId(currentUserId);
    }
}

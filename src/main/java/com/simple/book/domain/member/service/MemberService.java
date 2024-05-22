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

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    public String memberAddToProject(MemberMappingRequestDto memberMappingRequestDto){
        try {
            Project project = projectRepository.findById(memberMappingRequestDto.getProjectId())
                    .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + memberMappingRequestDto.getProjectId()));

            String currentUserId = userService.getCurrentUserId();
            User user = userRepository.findByAuthenticationUserId(currentUserId);
            if (user == null) {
                throw new EntityNotFoundException("User not found with authentication user ID: " + currentUserId);
            }

            Member member = Member.builder()
                    .project(project)
                    .isManager(memberMappingRequestDto.getIsManager())
                    .user(user)
                    .build();
            memberRepository.save(member);

            return "OK";
        } catch (EntityNotFoundException e) {
            log.error("Entity not found error occurred: {}", e.getMessage());
            // 추가적인 예외 처리 로직 추가 가능
            return "FAILED";
        }
    }
}

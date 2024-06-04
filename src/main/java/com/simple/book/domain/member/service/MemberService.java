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
    /**
     * @memberAddToProject
     * @Caution
     * 같은 유저를 여러개의 프로젝트에 추가하기 위해서 member table의 제약조건을 복합키로 설정해야 합니다.
     * 외래 키 제약 조건 제거
     ALTER TABLE member DROP FOREIGN KEY 외래키;

     * 기존 인덱스 제거
     ALTER TABLE member DROP INDEX 인덱스;

     * user_id와 project_id의 복합키로 고유 제약 조건 추가
     ALTER TABLE member ADD UNIQUE INDEX UK_user_project (userId, projectId);
    */
    @Transactional(rollbackFor = {Exception.class})
    public String memberAddToProject(MemberMappingRequestDto memberMappingRequestDto){
        Project project = projectRepository.findById(memberMappingRequestDto.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + memberMappingRequestDto.getProjectId()));

        String UserId = memberMappingRequestDto.getUserId();
        User user = userRepository.findByAuthenticationUserId(UserId);
        if (user == null) {
            throw new EntityNotFoundException("User not found with authentication user ID: " + UserId);
        }

        Member member = Member.builder()
                .project(project)
                .isManager(memberMappingRequestDto.getIsManager())
                .user(user)
                .build();
        memberRepository.save(member);

        return "OK";
    }
}

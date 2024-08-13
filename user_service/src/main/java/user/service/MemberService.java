package user.service;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import user.service.entity.Member;
import user.service.entity.User;
import user.service.global.advice.SuccessResponse;
import user.service.global.exception.EntityNotFoundException;
import user.service.global.exception.InvalidValueException;
import user.service.global.exception.MemberDuplicateInProjectException;
import user.service.kafka.member.KafkaMemberProducerService;
import user.service.kafka.member.event.RollbackMemberAddToProjectEvent;
import user.service.repository.MemberRepository;
import user.service.web.MemberInfoResponseDto;
import user.service.web.dto.member.request.MemberMappingToProjectRequestDto;
import user.service.web.dto.member.request.MemberMappingToTaskRequestDto;
import user.service.web.dto.project.response.GetUserIdsByProjectsResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {
    private final MemberRepository memberRepository;
    private final UserService userService;
    private final KafkaMemberProducerService kafkaMemberProducerService;
    /**
     * 프로젝트에 멤버를 추가합니다.
     * @param memberMappingToProjectRequestDto
     * @return
     */
//    @Transactional(rollbackFor = { Exception.class })
//    public SuccessResponse memberAddToProject(MemberMappingToProjectRequestDto memberMappingToProjectRequestDto) {
//        List<String> userIds = memberMappingToProjectRequestDto.getUserIds();
//        Long projectId = memberMappingToProjectRequestDto.getProjectId();
//        int isManager = memberMappingToProjectRequestDto.getIsManager();
//
//        userIds.forEach(userId -> {
//            try {
//                User user = userService.findUserEntity(userId);
//                Member member = Member.builder()
//                        .isManager(isManager)
//                        .projectId(projectId)
//                        .user(user)
//                        .build();
//                memberRepository.save(member);
//            } catch (DataIntegrityViolationException e) {
//                throw new MemberDuplicateInProjectException(e.getMessage());
//            }
//        });
//        //프로젝트 존재하지 않을시 보상 트랜잭션 처리
//        kafkaMemberProducerService.isExistProjectByMemberAddToProject(projectId, userIds);
//        return new SuccessResponse("멤버 추가 성공", userIds);
//    }
    // New
    // 수정자 : 강민경
    @Transactional(rollbackFor = { Exception.class })
    public SuccessResponse memberAddToProject(MemberMappingToProjectRequestDto memberMappingToProjectRequestDto) {
        List<String> userIds = memberMappingToProjectRequestDto.getUserIds();
        Long projectId = memberMappingToProjectRequestDto.getProjectId();
        int isManager = memberMappingToProjectRequestDto.getIsManager();

        userIds.forEach(userId -> {
            try {
                User user = userService.findUserEntity(userId);
                Member member = Member.builder()
                        .isManager(isManager)
                        .projectId(projectId)
                        .user(user)
                        .build();
                memberRepository.save(member);
            } catch (DataIntegrityViolationException e) {
                throw new MemberDuplicateInProjectException(e.getMessage());
            }
        });
        //프로젝트 존재하지 않을시 보상 트랜잭션 처리
        kafkaMemberProducerService.isExistProjectByMemberAddToProject(projectId, userIds);
        return SuccessResponse.builder().message("멤버 추가 성공").data(userIds).build();
//        return new SuccessResponse("멤버 추가 성공", userIds);
    }
    
    /**
     * 프로젝트에 속한 멤버를 조회합니다.
     * @param projectId, userId
     * @return
     */
    @Transactional(rollbackFor = { Exception.class })
    public Member findMemberByUserIdAndProjectId(Long userId, Long projectId) {
        return memberRepository.findMemberByUserIdAndProjectId(userId, projectId)
                .orElseThrow(() -> new EntityNotFoundException("Member not found with UserId: " + userId + " and ProjectId: " + projectId));
    }
    /**
     * 멤버가 관리자인지 확인합니다.
     * @param memberId
     * @return
     */
    @Transactional(rollbackFor = { Exception.class })
    public void isManager(Long memberId) {
        Optional<Member> member = memberRepository.findById(memberId);
        if (member.get().getIsManager() != 1 && member.get().getIsManager() != 2) {
            throw new InvalidValueException("해당 멤버는 생성자가 아닙니다.");
        }
    }
    /**
     * 모든 멤버가 같은 프로젝트에 소속되어 있는지 확인합니다.
     * @param memberMappingToTaskRequestDto
     * @return
     */
//    @Transactional(rollbackFor = { Exception.class })
//    public SuccessResponse allMembersInSameProject(MemberMappingToTaskRequestDto memberMappingToTaskRequestDto) {
//        List<Long> memberIds = memberMappingToTaskRequestDto.getMemberIds();
//        Set<Long> uniqueProjectIds = memberIds.stream()
//                .map(memberId -> memberRepository.findById(memberId)
//                        .orElseThrow(() -> new EntityNotFoundException("Member not found for ID: " + memberId)))
//                .map(Member::getProjectId)
//                .collect(Collectors.toSet());
//
//        if (uniqueProjectIds.size() == 1) {
//            // 모든 멤버가 같은 프로젝트에 속해 있을 경우
//            List<Long> userIds = memberIds.stream()
//                    .map(memberId -> memberRepository.findById(memberId).get().getUser().getId())
//                    .collect(Collectors.toList());
//            return new SuccessResponse("모든 멤버가 같은 프로젝트에 속해 있습니다.", userIds);
//        } else {
//            // 멤버들이 서로 다른 프로젝트에 속해 있을 경우
//            return new SuccessResponse("모든 멤버가 같은 프로젝트에 속해 있지 않습니다.", memberIds);
//        }
//    }
    // New
    // 수정자 : 강민경
    @Transactional(rollbackFor = { Exception.class })
    public SuccessResponse allMembersInSameProject(MemberMappingToTaskRequestDto memberMappingToTaskRequestDto) {
        List<Long> memberIds = memberMappingToTaskRequestDto.getMemberIds();
        Set<Long> uniqueProjectIds = memberIds.stream()
                .map(memberId -> memberRepository.findById(memberId)
                        .orElseThrow(() -> new EntityNotFoundException("Member not found for ID: " + memberId)))
                .map(Member::getProjectId)
                .collect(Collectors.toSet());

        if (uniqueProjectIds.size() == 1) {
            // 모든 멤버가 같은 프로젝트에 속해 있을 경우
            List<Long> userIds = memberIds.stream()
                    .map(memberId -> memberRepository.findById(memberId).get().getUser().getId())
                    .collect(Collectors.toList());
            return SuccessResponse.builder().message("모든 멤버가 같은 프로젝트에 속해 있습니다.").data(userIds).build();
        } else {
            // 멤버들이 서로 다른 프로젝트에 속해 있을 경우
            return SuccessResponse.builder().message("모든 멤버가 같은 프로젝트에 속해 있지 않습니다.").data(memberIds).build();
        }
    }
    
    /**
     * 프로젝트에 속해있는 멤버들을 삭제합니다.
     * @param projectId
     * @return
     */
    @Transactional(rollbackFor = { Exception.class })
    public void deleteMembersByProjectId(Long projectId) {
        List<Member> members = memberRepository.findByProjectId(projectId);
        memberRepository.deleteAll(members);
    }
    /**
     * 사용자가 속한 프로젝트 ID를 조회합니다.
     * @param userId
     * @return
     */
    @Transactional(rollbackFor = { Exception.class })
    public List<Long> getProjectIdsByUserId(Long userId) {
        return memberRepository.findProjectIdsByUserId(userId);
    }
    /**
     * 프로젝트에 속한 멤버 ID를 조회합니다.
     * @param projectIds
     * @return
     */
    public SuccessResponse getUsersFromProjects(List<Long> projectIds) {
        List<GetUserIdsByProjectsResponseDto> dto = projectIds.stream()
            .map(projectId -> {
                List<User> users = memberRepository.findMemberIdsByProjectId(projectId);
                List<Long> userIds = users.stream()
                    .map(User::getId)
                    .collect(Collectors.toList());
                if (userIds.isEmpty()) {
                    throw new EntityNotFoundException("No members found for ProjectId: " + projectId);
                }
                return new GetUserIdsByProjectsResponseDto(projectId, userIds);
            })
            .collect(Collectors.toList());
        return SuccessResponse.builder()
                .message("프로젝트 멤버 조회 성공")
                .data(dto)
                .build();
    }

    public void rollbackMemberAddToProject(RollbackMemberAddToProjectEvent event) {
        List<String> userIds = event.getUserIds();
        Long projectId = event.getProjectId();
        userIds.forEach(userId -> {
            Member member = memberRepository.findMemberByUserIdAndProjectId(userService.getUserEntityId(userId), projectId)
                    .orElseThrow(() -> new EntityNotFoundException("Member not found with UserId: " + userId + " and ProjectId: " + projectId));
            memberRepository.delete(member);
        });
    }
    public SuccessResponse getMembersByUserIds(List<Long> userIds) {
        List<Member> members = memberRepository.findMembersByUserIds(userIds);
        List<MemberInfoResponseDto> dtos =
            members.stream()
                .map(member -> {
                    MemberInfoResponseDto dto = new MemberInfoResponseDto();
                    dto.setUserId(member.getUser().getId());
                    dto.setProjectId(member.getProjectId());
                    dto.setIsManager(member.getIsManager());
                    return dto;
                })
                .collect(Collectors.toList());
        return SuccessResponse.builder()
                .message("멤버 조회 성공")
                .data(dtos)
                .build();
    }
}

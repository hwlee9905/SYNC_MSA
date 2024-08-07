package user.service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import user.service.global.exception.InvalidValueException;
import user.service.kafka.member.KafkaMemberProducerService;
import user.service.kafka.member.event.RollbackMemberAddToProjectEvent;
import user.service.web.dto.member.request.MemberMappingToProjectRequestDto;
import user.service.entity.Member;
import user.service.entity.User;
import user.service.global.advice.SuccessResponse;
import user.service.global.exception.EntityNotFoundException;
import user.service.global.exception.MemberDuplicateInProjectException;
import user.service.repository.MemberRepository;
import user.service.web.dto.member.request.MemberMappingToTaskRequestDto;
import user.service.web.dto.project.response.GetUserIdsByProjectsResponseDto;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
        return new SuccessResponse("멤버 추가 성공", true, userIds);
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
            return new SuccessResponse("모든 멤버가 같은 프로젝트에 속해 있습니다.", true, userIds);
        } else {
            // 멤버들이 서로 다른 프로젝트에 속해 있을 경우
            return new SuccessResponse("모든 멤버가 같은 프로젝트에 속해 있지 않습니다.", false, memberIds);
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
    public List<GetUserIdsByProjectsResponseDto> getUsersFromProjects(List<Long> projectIds) {
        return projectIds.stream()
                .map(projectId -> {
                    List<Long> userIds = memberRepository.findMemberIdsByProjectId(projectId);
                    if (userIds.isEmpty()) {
                        throw new EntityNotFoundException("No members found for ProjectId: " + projectId);
                    }
                    return new GetUserIdsByProjectsResponseDto(projectId, userIds);
                })
                .collect(Collectors.toList());
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
}

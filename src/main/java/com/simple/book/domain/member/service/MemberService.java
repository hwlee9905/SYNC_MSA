package com.simple.book.domain.member.service;


import com.simple.book.domain.alarm.service.AlarmService;
import com.simple.book.domain.member.dto.request.MemberMappingToProjectRequestDto;
import com.simple.book.domain.member.dto.request.MemberMappingToTaskRequestDto;
import com.simple.book.domain.member.entity.Member;
import com.simple.book.domain.member.entity.MemberTask;
import com.simple.book.domain.member.entity.MemberTaskId;
import com.simple.book.domain.member.repository.MemberRepository;
import com.simple.book.domain.member.repository.TaskMemberRepository;
import com.simple.book.domain.member.repository.UserTaskRepository;
import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.project.repository.ProjectRepository;
import com.simple.book.domain.task.entity.Task;
import com.simple.book.domain.task.repository.TaskRepository;
import com.simple.book.domain.user.entity.User;
import com.simple.book.domain.user.entity.UserTask;
import com.simple.book.domain.user.entity.UserTaskId;
import com.simple.book.domain.user.repository.UserRepository;
import com.simple.book.global.exception.EntityNotFoundException;
import com.simple.book.global.exception.InvalidValueException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    private final TaskRepository taskRepository;
    private final TaskMemberRepository taskMemberRepository;
    private final UserTaskRepository userTaskRepository;
    
    @Autowired
    private AlarmService alarmService;
    /**
    * @memberAddToProject
    * @Caution
    * 같은 유저를 여러개의 프로젝트에 추가하기 위해서 member table의 unique 제약조건을 복합키로 설정해야 합니다.

    * user_id와 project_id의 복합키로 고유 제약 조건 추가
    ALTER TABLE member ADD CONSTRAINT unique_user_project UNIQUE (user_id, project_id);
    */
    @Transactional(rollbackFor = {Exception.class})
    public Member memberAddToProject(MemberMappingToProjectRequestDto memberMappingToProjectRequestDto){
        Project project = projectRepository.findById(memberMappingToProjectRequestDto.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found with ID: " + memberMappingToProjectRequestDto.getProjectId()));

        String UserId = memberMappingToProjectRequestDto.getUserId();
        User user = userRepository.findByAuthenticationUserId(UserId);
        if (user == null) {
            throw new EntityNotFoundException("User not found with authentication user ID: " + UserId);
        }

        Member member = Member.builder()
                .project(project)
                .isManager(memberMappingToProjectRequestDto.getIsManager())
                .user(user)
                .build();
        try {
        	memberRepository.save(member);
        } catch (DataIntegrityViolationException e) {
        	throw new RuntimeException("data");
        }

        return member;
    }
    @Transactional(rollbackFor = {Exception.class})
    public Member memberAddToTask(MemberMappingToTaskRequestDto memberMappingToTaskRequestDto){

        Optional<Member> optionalMember = memberRepository.findById(memberMappingToTaskRequestDto.getMemberId());
        Member member = optionalMember.orElseThrow(() -> new EntityNotFoundException("Member not found with Member ID: " + memberMappingToTaskRequestDto.getMemberId()));
        Optional<Task> optionalTask = taskRepository.findById(memberMappingToTaskRequestDto.getTaskId());
        Task task = optionalTask.orElseThrow(() -> new EntityNotFoundException("Task not found with Task ID: " + memberMappingToTaskRequestDto.getTaskId()));
        if(task.getProject().equals(member.getProject())){
            User user = userRepository.getReferenceById(member.getUser().getId());
            UserTaskId userTaskId = UserTaskId.builder()
                    .taskId(task.getId())
                    .userId(user.getId())
                    .build();
            UserTask userTask = UserTask.builder()
                    .id(userTaskId)
                    .user(user)
                    .task(task)
                    .build();
            MemberTaskId memberTaskId = MemberTaskId.builder()
                    .mappingTaskId(task.getId())
                    .mappingMemberId(member.getId())
                    .build();
            MemberTask memberTask = MemberTask.builder()
                    .id(memberTaskId)
                    .member(member)
                    .task(task)
                    .build();
	        try {
	            taskMemberRepository.saveAndFlush(memberTask);
                userTaskRepository.saveAndFlush(userTask);
	        } catch (DataIntegrityViolationException e) {
	        	throw new RuntimeException("해당 업무에 이미 등록된 담당자가 있습니다.");
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
	        alarmService.sendTaskManager(memberMappingToTaskRequestDto.getMemberId());
        
        }else{
            throw new InvalidValueException("해당 작업과 멤버는 같은 프로젝트 소속이어야 합니다.");
        }

        return member;
    }
}

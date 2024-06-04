package com.simple.book.domain.member.repository;

import com.simple.book.domain.member.entity.TaskMember;
import com.simple.book.domain.member.entity.TaskMemberId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskMemberRepository extends JpaRepository<TaskMember, TaskMemberId> {
}

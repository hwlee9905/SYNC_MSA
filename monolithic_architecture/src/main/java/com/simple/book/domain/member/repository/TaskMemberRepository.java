package com.simple.book.domain.member.repository;

import com.simple.book.domain.member.entity.MemberTask;
import com.simple.book.domain.member.entity.MemberTaskId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskMemberRepository extends JpaRepository<MemberTask, MemberTaskId> {

}

package com.simple.book.domain.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.simple.book.domain.member.entity.Member;
import com.simple.book.domain.user.entity.User;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    @Query("SELECT m FROM Member m WHERE m.user.id = :userId AND m.project.id = :projectId")
    Optional<Member> findByUserIdAndProjectId(@Param("userId") Long userId, @Param("projectId") Long projectId);
    
//    @Query("SELECT m.user.id FROM Member m INNER JOIN m.taskMembers tm WHERE tm.id.mappingMemberId = :memberId")
//    Optional<Long> findUserIdByTaskMember(@Param("memberId") long memberId);
    
    @Query("SELECT u FROM Member m INNER JOIN m.user u WHERE m.id = :memberId")
    Optional<User> findUserIdByTaskMember(@Param("memberId") long memberId);
}

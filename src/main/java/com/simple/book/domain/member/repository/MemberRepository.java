package com.simple.book.domain.member.repository;

import com.simple.book.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    @Query("SELECT m FROM Member m WHERE m.user.id = :userId AND m.project.id = :projectId")
    Optional<Member> findByUserIdAndProjectId(@Param("userId") Long userId, @Param("projectId") Long projectId);
}

package com.simple.book.domain.member.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.simple.book.domain.user.entity.UserTask;

public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
    void deleteByTaskId(Long taskId);

	List<UserTask> findByUserId(Long userId);
}

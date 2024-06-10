package com.simple.book.domain.member.repository;

import com.simple.book.domain.user.entity.UserTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserTaskRepository extends JpaRepository<UserTask, Long> {
    void deleteByTaskId(Long taskId);
}

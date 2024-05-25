package com.simple.book.domain.task.repository;

import com.simple.book.domain.project.entity.Project;
import com.simple.book.domain.task.dto.response.GetTaskResponseDto;
import com.simple.book.domain.task.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task,Long> {
    Optional<Task> findById(Optional<Long> parentTaskId);
    @Query("SELECT t FROM Task t")
    List<GetTaskResponseDto> findAllProjectedBy();
}

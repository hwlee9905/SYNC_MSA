package project.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.service.entity.UserTask;
import project.service.entity.UserTaskId;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, UserTaskId>{
    List<UserTask> findByTaskId(Long taskId);
    @Query("SELECT ut FROM UserTask ut WHERE ut.id.userId = :userId AND ut.id.taskId = :taskId")
    Optional<UserTask> findByUserIdAndTaskId(@Param("userId") Long userId, @Param("taskId") Long taskId);
}

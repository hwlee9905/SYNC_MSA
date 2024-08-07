package project.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.service.entity.Task;

import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {
    Optional<Task> findById(Optional<Long> parentTaskId);
    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.id = :projectId AND t.depth = 0")
    int countByProjectIdAndDepth(Long projectId);
    @Query("SELECT COUNT(t) FROM Task t WHERE t.project.id = :projectId AND t.depth = 0 AND t.status = 2")
    int countByProjectIdAndDepthAndStatus(Long projectId);
}

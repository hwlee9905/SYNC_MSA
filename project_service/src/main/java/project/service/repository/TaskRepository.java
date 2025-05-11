package project.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
    @Query("SELECT (SUM(CASE WHEN t.status = 2 THEN 1 ELSE 0 END) * 1.0 / COUNT(t)) FROM Task t WHERE t.project.id = :projectId AND t.depth = 0")
    Float countTotalAndCompletedTasksByProjectId(Long projectId);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.subTasks WHERE t.id = :taskId")
    Optional<Task> findTaskWithSubTasks(@Param("taskId") Long taskId);
}

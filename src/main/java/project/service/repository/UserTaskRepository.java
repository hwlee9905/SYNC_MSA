package project.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.service.entity.UserTask;
import project.service.entity.UserTaskId;

import java.util.List;

@Repository
public interface UserTaskRepository extends JpaRepository<UserTask, UserTaskId>{
    List<UserTask> findByTaskId(Long taskId);
}

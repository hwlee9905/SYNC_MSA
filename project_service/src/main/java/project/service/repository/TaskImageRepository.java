package project.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.service.entity.TaskImage;

import java.util.List;

public interface TaskImageRepository extends JpaRepository<TaskImage, Long> {
    List<TaskImage> findByTaskId(Long taskId);
}
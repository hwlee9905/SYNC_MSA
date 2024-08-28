package project.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.service.entity.TaskImage;

public interface TaskImageRepository extends JpaRepository<TaskImage, Long> {
}
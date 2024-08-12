package project.service.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import project.service.entity.Invite;
import project.service.entity.Project;

@Repository
public interface InviteRepository extends JpaRepository<Invite, String>{
	void deleteByProject(Project project);
	boolean existsByToken(UUID token);
}

package user.service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import user.service.entity.Invite;

@Repository
public interface InviteRepository extends JpaRepository<Invite, String>{
	void deleteByProjectId(Long projectId);
	
	boolean existsByToken(UUID token);
	
	Optional<Invite> findByProjectId(Long projectId);
	
	Optional<Invite> findByToken(UUID token);
}

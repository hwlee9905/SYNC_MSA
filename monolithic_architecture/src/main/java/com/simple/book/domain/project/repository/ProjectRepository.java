package com.simple.book.domain.project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.simple.book.domain.project.entity.Invite;
import com.simple.book.domain.project.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
	
	@Query("SELECT i FROM Project p INNER JOIN p.invite i WHERE p.id = :projectId")
	Optional<Invite> findInviteByProjectId(@Param("projectId")long projectId);
}

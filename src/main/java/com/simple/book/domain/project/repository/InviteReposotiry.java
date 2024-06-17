package com.simple.book.domain.project.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.book.domain.project.entity.Invite;
import com.simple.book.domain.project.entity.Project;

@Repository
public interface InviteReposotiry extends JpaRepository<Invite, String>{
	void deleteByProject(Project project);

	Optional<Invite> findByProject(Project project);

	boolean existsByToken(UUID token);
}

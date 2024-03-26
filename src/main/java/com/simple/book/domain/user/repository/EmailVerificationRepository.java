package com.simple.book.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.book.domain.user.entity.EmailVerification;

@Repository
public interface EmailVerificationRepository extends JpaRepository<EmailVerification, String>{
	Optional<EmailVerification> findByEmail(String email);

	int deleteByEmail(String email);

}

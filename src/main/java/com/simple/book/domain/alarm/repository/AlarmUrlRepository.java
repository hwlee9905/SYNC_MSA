package com.simple.book.domain.alarm.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.simple.book.domain.alarm.entity.AlarmUrl;
import com.simple.book.domain.user.entity.User;

@Repository
public interface AlarmUrlRepository extends JpaRepository<AlarmUrl, UUID>{

	@Query("SELECT au.url FROM AlarmUrl au JOIN au.user u JOIN u.authentication a WHERE a.userId = :userId")
	Optional<UUID> findUrlByAuthenticationUserId(@Param("userId") String userId);

	@Query("SELECT a.url FROM AlarmUrl a WHERE a.user = :user")
	Optional<UUID> findUrlByUser(@Param("user") User user);
}

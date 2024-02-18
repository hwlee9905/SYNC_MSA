package com.simple.book.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.book.entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u WHERE u.id = :id")
	boolean existsById(String id);
	
	@Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserEntity u WHERE u.id = :id AND u.password = :password")
	boolean existsByIdAndPassword(String id, String password);
	
	UserEntity findAllById(String id);
}

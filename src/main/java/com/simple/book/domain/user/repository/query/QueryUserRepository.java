package com.simple.book.domain.user.repository.query;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.simple.book.domain.user.entity.UserEntity;

@Repository
public interface QueryUserRepository extends JpaRepository<UserEntity, String> {
	@Query(value = "SELECT * FROM USERTBL WHERE ID LIKE :keyword", nativeQuery = true)
	List<UserEntity> findByIdLikeKeyword(@Param("keyword") String keyword);
	
	@Query(value = "SELECT U.NAME FROM (SELECT CONCAT(FIRST_NAME, LAST_NAME) AS NAME FROM USERTBL) AS U WHERE U.NAME LIKE :keyword", nativeQuery = true)
	List<UserEntity> findByNameLikeKeyword(@Param("keyword") String keyword);
}

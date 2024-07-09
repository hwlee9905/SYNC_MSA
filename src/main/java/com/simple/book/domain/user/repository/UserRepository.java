package com.simple.book.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.simple.book.domain.user.dto.UserInfo;
import com.simple.book.domain.user.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByAuthenticationUserId(String userId);
    
    @Query("SELECT u.username AS username, u.nickname AS nickname, u.position AS position, u.introduction AS introduction, u.profileImg AS profileImg, a.userId AS userId FROM User u INNER JOIN u.authentication a WHERE u.id = :id")
    Optional<UserInfo> findUserAndAuthenticationById(@Param("id") long id);
}

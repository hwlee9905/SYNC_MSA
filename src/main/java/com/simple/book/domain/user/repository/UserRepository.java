package com.simple.book.domain.user.repository;

import com.simple.book.domain.user.entity.User;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByAuthenticationUserId(String userId);
    @Query("SELECT u.id FROM User u WHERE u.authentication.userId = :userId")
    Optional<Long> findUserByAuthenticationUserId(@Param("userId") String userId);
}

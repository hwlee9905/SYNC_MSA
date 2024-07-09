package com.simple.book.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.book.domain.user.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByAuthenticationUserId(String userId);
}

package com.simple.book.domain.user.repository;

import com.simple.book.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT u.* FROM user u JOIN authentication a ON u.id = a.id WHERE a.userId = :userId", nativeQuery = true)
    User findByUserId(@Param("userId") String userId);
}

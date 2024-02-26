package com.simple.book.repository.query;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.book.entity.UserEntity;

@Repository
public interface QueryUserRepository  extends JpaRepository<UserEntity, String> {
}

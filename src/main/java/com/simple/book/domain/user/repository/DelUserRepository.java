package com.simple.book.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.book.domain.user.entity.DelUserEntity;

@Repository
public interface DelUserRepository extends JpaRepository<DelUserEntity, Integer>{

}

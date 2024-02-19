package com.simple.book.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.book.entity.DelUserEntity;

@Repository
public interface DelUserRepository extends JpaRepository<DelUserEntity, Integer>{

}

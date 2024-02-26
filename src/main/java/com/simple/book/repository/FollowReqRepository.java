package com.simple.book.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.book.entity.FollowReqEntity;

@Repository
public interface FollowReqRepository extends JpaRepository<FollowReqEntity, Integer> {

	Optional<FollowReqEntity> findByIdAndReqIdAndAcceptYn(String id, String reqId, String acceptYn);
}

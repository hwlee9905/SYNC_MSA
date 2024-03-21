package com.simple.book.domain.follow.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.book.domain.follow.entity.FollowReqEntity;

@Repository
public interface FollowReqRepository extends JpaRepository<FollowReqEntity, Integer> {
	Optional<FollowReqEntity> findByIdAndReqIdAndAcceptYn(String id, String reqId, String acceptYn);
}

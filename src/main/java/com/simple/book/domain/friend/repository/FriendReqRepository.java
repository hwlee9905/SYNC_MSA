package com.simple.book.domain.friend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.book.domain.friend.entity.FriendReqEntity;

@Repository
public interface FriendReqRepository extends JpaRepository<FriendReqEntity, Integer> {
	List<FriendReqEntity> findByIdAndAcceptYn(String id, String acceptYn);

	Optional<FriendReqEntity> findByIdAndReqIdAndAcceptYn(String id, String reqId, String acceptYn);
	
	List<FriendReqEntity> findByIdOrReqId(String id, String reqId);
	
	List<FriendReqEntity> findByReqIdAndAcceptYn(String reqId, String acceptYn);
}

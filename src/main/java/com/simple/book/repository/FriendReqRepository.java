package com.simple.book.repository;

import java.util.List;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.simple.book.entity.FriendReqEntity;

@Repository
public interface FriendReqRepository extends JpaRepository<FriendReqEntity, Integer>{
	
	@Query("SELECT f FROM FriendReqEntity f WHERE f.id = :id AND f.reqId = :myId")
	FriendReqEntity findByIdAndReqId(String id, String myId);
	
	@Query("SELECT f.id, f.reqId FROM FriendReqEntity f WHERE (f.id = :id OR f.reqId = :id) AND e.acceptYn:acceptYn")
	List<FriendReqEntity> findByIdAndAcceptYn(String id, String acceptYn);
	
	@Query("SELECT f.id, f.reqId FROM FriendReqEntity f WHERE f.id = :id OR f.reqId = :id")
	List<FriendReqEntity> findById(String id);
}

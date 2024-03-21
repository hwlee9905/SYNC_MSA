package com.simple.book.domain.friend.repository.query;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.simple.book.domain.friend.entity.FriendReqEntity;

@Repository
public interface QueryFriendReqRepository extends JpaRepository<FriendReqEntity, Integer> {
	@Query(value = "SELECT * FROM FRIENDREQTBL WHERE (ID = :id AND REQ_ID = :reqId) OR (ID = :reqId AND REQ_ID = :id)", nativeQuery = true)
	Optional<FriendReqEntity> findByIdOrReqId(@Param("id") String id, @Param("reqId") String reqId);
	
	@Query(value = "SELECT CASE WHEN ID = :id THEN REQ_ID WHEN REQ_ID = :id THEN ID END FROM FRIENDREQTBL WHERE (ID = :id OR REQ_ID = :id) AND ACCEPT_YN = :acceptYn", nativeQuery = true)
	List<String> findByIdAndAcceptYn(@Param("id") String id, @Param("acceptYn") String acceptYn);
}

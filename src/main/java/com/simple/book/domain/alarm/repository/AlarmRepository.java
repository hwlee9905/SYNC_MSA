package com.simple.book.domain.alarm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.simple.book.domain.alarm.entity.Alarm;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long>{

	@Query("SELECT a FROM alarm a INNER JOIN a.user u INNER JOIN u.authentication auth WHERE auth.userId = :userId")
	List<Alarm> findByUserId(@Param("userId") String userId);

//	int deleteByName(String topicname);
//
//	List<Alarm> findByType(String type);

}
